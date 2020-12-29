package com.workuptrackerbot.service;

import com.workuptrackerbot.entity.Project;
import com.workuptrackerbot.entity.UserEntity;
import com.workuptrackerbot.entity.UserProject;
import com.workuptrackerbot.repository.ProjectRepository;
import com.workuptrackerbot.repository.UPRepository;
import com.workuptrackerbot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UPRepository upRepository;

    public List<UserProject> addProject(String username, String projectName) throws Exception {
        /*todo
        Найти юзера
        Проверить нет ли такого проекта, если нет, создать
        Вернуть проект
         */

        UserEntity user = userRepository.findByUsername(username);
        //если проект уже существует
        if(user.getProjects().stream().anyMatch(project -> projectName.equals(project.getName()))){
            throw new Exception("project is already exist");
        }

        Project project = new Project(projectName);
        projectRepository.save(project);

        UserProject userProject = new UserProject(user, project, true);
        upRepository.save(userProject);

        return upRepository.findByUserEntity(user);
    }
}
