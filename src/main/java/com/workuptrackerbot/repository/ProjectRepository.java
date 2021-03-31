package com.workuptrackerbot.repository;

import com.workuptrackerbot.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>{
    Project findByName(String name);

    Optional<Project> findById(Long id);
}
