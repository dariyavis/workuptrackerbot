package com.workuptrackerbot.service;

import com.workuptrackerbot.entity.Project;
import com.workuptrackerbot.entity.UserEntity;
import com.workuptrackerbot.entity.UserProject;
import com.workuptrackerbot.repository.ProjectRepository;
import com.workuptrackerbot.repository.UPRepository;
import com.workuptrackerbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Predicate;

@Service
public class ProjectService {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UPRepository upRepository;

    public void addProject(Integer user_id, String projectName) throws Exception {
        /*todo
        Найти юзера
        Проверить нет ли такого проекта, если нет, создать
        Вернуть проект
         */

        UserEntity user = userService.getUser(user_id);
        //если проект уже существует
        if(user.getProjects().stream().anyMatch(project -> projectName.equals(project.getName()))){
            throw new Exception("project is already exist");
        }

        Project project = new Project(projectName);
        projectRepository.save(project);

        UserProject userProject = new UserProject(user, project, true);
        upRepository.save(userProject);
    }

    public List<Project> getProjects(Integer user_id){
        return userService.getUser(user_id).getProjects();
    }

    @Transactional
    public void removeProject(Integer user_id, String projectName) throws Exception{
        UserEntity user = userService.getUser(user_id);
        Project project = user.getProjects().stream()
                .filter(pro -> projectName.equals(pro.getName())).findFirst().orElseThrow();
        upRepository.deleteByUserEntityAndProject(user, project);
        projectRepository.delete(project);
    }
}
