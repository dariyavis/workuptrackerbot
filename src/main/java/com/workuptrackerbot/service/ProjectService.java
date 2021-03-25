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
        if(upRepository.findByUserEntityIdAndProjectName(user_id, projectName) != null){
            throw new Exception("project is already exist");
        }

        Project project = new Project(projectName);
        projectRepository.save(project);

        UserProject userProject = new UserProject(user, project);
        upRepository.save(userProject);
    }

//    public List<Project> getProjects(Integer user_id){
//        return userService.getUser(user_id).getProjects();
//    }

    public List<Project> getActiveProjects(Integer user_id){
        List<UserProject> ups = upRepository.findByUserEntityIdAndActive(user_id, true);
        return ups.stream().map(UserProject::getProject).collect(Collectors.toList());
    }

    public List<Project> getArchiveProjects(Integer user_id){
        List<UserProject> ups = upRepository.findByUserEntityIdAndActive(user_id, false);
        return ups.stream().map(UserProject::getProject).collect(Collectors.toList());
    }


    public List<Project> getProjects(Integer user_id){
        List<UserProject> ups = upRepository.findByUserEntityId(user_id);
        return ups.stream().map(UserProject::getProject).collect(Collectors.toList());
    }

    @Transactional
    public Project removeProjectById(Integer user_id, String project_id) {
        UserEntity user = userService.getUser(user_id);
        UserProject up = upRepository.findByUserEntityIdAndProjectId(user_id, Long.valueOf(project_id));
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
        UserProject up = upRepository.findByUserEntityIdAndProjectId(user_id, Long.valueOf(project_id));
        up.setActive(active);
        upRepository.save(up);
        return up.getProject();
    }


    public UserProject getProjectInfoById(Integer user_id, String project_id) {
        return upRepository.findByUserEntityIdAndProjectId(user_id, Long.valueOf(project_id));
    }

    public void renameProject(Integer user_id, String project_name, String new_project_name) {
        UserProject up = upRepository.findByUserEntityIdAndProjectName(user_id, project_name);
        Project project = up.getProject();
        project.setName(new_project_name);
        projectRepository.save(project);
    }
}
