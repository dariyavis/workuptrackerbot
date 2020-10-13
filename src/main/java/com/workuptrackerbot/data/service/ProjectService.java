package com.workuptrackerbot.data.service;

import com.workuptrackerbot.data.entity.Project;
import com.workuptrackerbot.data.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
