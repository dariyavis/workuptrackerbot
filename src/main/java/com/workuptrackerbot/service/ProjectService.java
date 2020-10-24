package com.workuptrackerbot.service;

import com.workuptrackerbot.entity.Project;
import com.workuptrackerbot.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;

//@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

//    public ProjectService(ProjectRepository projectRepository){
//        this.projectRepository = projectRepository;
//    }

    public void createProject(Project project){
//        projectRepository.save(project);
    }
}
