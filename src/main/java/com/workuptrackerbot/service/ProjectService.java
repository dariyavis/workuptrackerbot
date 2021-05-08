package com.workuptrackerbot.service;

import com.workuptrackerbot.entity.Project;
import com.workuptrackerbot.entity.UserEntity;
import com.workuptrackerbot.entity.UserProject;
import com.workuptrackerbot.repository.IntervalRepository;
import com.workuptrackerbot.repository.ProjectRepository;
import com.workuptrackerbot.repository.UPRepository;
import com.workuptrackerbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UPRepository upRepository;

    @Autowired
    private IntervalService intervalService;

    public void addProject(Integer user_id, String projectName) throws Exception {

        UserEntity user = userService.getUser(user_id);
        //если проект уже существует
        if(upRepository.findByUserEntity_TlgIdAndProjectName(user_id, projectName) != null){
            throw new Exception("project is already exist");
        }

        Project project = new Project(projectName);
        projectRepository.save(project);

        UserProject userProject = new UserProject(user, project);
        upRepository.save(userProject);
    }

    public UserProject addUserToProject(UserEntity user, String project_Id) {

        Project project = getProject(project_Id);
//        if(project==null) {
//            throw new Exception("project not found");
//        }

        UserProject userProject = new UserProject(user, project, false);
        upRepository.save(userProject);
        return userProject;
    }

    public Project getProject(String project_Id){
        Optional<Project> opt = projectRepository.findById(Long.valueOf(project_Id));
        return opt.orElse(null);
    }

    public List<Project> getActiveProjects(Integer user_id){
        List<UserProject> ups = upRepository.findByUserEntity_TlgIdAndActive(user_id, true);
        return ups.stream().map(UserProject::getProject).collect(Collectors.toList());
    }

    public List<Project> getArchiveProjects(Integer user_id){
        List<UserProject> ups = upRepository.findByUserEntity_TlgIdAndActive(user_id, false);
        return ups.stream().map(UserProject::getProject).collect(Collectors.toList());
    }


    public List<Project> getProjects(Integer user_id){
        List<UserProject> ups = getUPs(user_id);
        return ups.stream().map(UserProject::getProject).collect(Collectors.toList());
    }
    public List<UserProject> getUPs(Integer user_id){
        return upRepository.findByUserEntity_TlgId(user_id);
    }

    @Transactional
    public Project removeProjectById(Integer user_id, String project_id) {
        UserEntity user = userService.getUser(user_id);
        UserProject up = upRepository.findByUserEntity_TlgIdAndProjectId(user_id, Long.valueOf(project_id));
        if(up == null) {
            return null;
        }
        intervalService.deleteIntervals(up);
        upRepository.delete(up);
        projectRepository.delete(up.getProject());
        return up.getProject();
    }


    public Project unzipProjectById(Integer user_id, String project_id) {
        return archiveProjectById(user_id, project_id, true);
    }
    public Project zipProjectById(Integer user_id, String project_id) {
        return archiveProjectById(user_id, project_id, false);
    }

    private Project archiveProjectById(Integer user_id, String project_id, boolean active) {
        UserProject up = upRepository.findByUserEntity_TlgIdAndProjectId(user_id, Long.valueOf(project_id));
        up.setActive(active);
        upRepository.save(up);
        return up.getProject();
    }


    public UserProject getProjectInfoById(Integer user_id, String project_id) {
        return upRepository.findByUserEntity_TlgIdAndProjectId(user_id, Long.valueOf(project_id));
    }

    public Project getProjectInfoById(String project_id) {
        Optional<Project> opt = projectRepository.findById(Long.valueOf(project_id));
        return opt.orElse(null);
    }

    public Project renameProject(Integer user_id, String project_id, String new_project_name)  throws Exception{
        if(upRepository.findByUserEntity_TlgIdAndProjectName(user_id, new_project_name) != null){
            throw new Exception("project is already exist");
        }
        Project project = getProjectInfoById(project_id);
        if(project !=null) {
            project.setName(new_project_name);
            projectRepository.save(project);
        }
        return project;
    }
}
