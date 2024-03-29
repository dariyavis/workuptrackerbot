package com.workuptrackerbot.repository;

import com.workuptrackerbot.entity.Project;
import com.workuptrackerbot.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>{
    public Project findByName(String name);
}
