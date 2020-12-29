package com.workuptrackerbot.repository;

import com.workuptrackerbot.entity.Project;
import com.workuptrackerbot.entity.UserEntity;
import com.workuptrackerbot.entity.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UPRepository extends JpaRepository<UserProject, Long>{

    List<UserProject> findByUserEntity(UserEntity user);

}
