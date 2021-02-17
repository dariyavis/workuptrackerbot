package com.workuptrackerbot.repository;

import com.workuptrackerbot.entity.CommandStateEntity;
import com.workuptrackerbot.entity.Project;
import com.workuptrackerbot.entity.UserEntity;
import com.workuptrackerbot.entity.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandStateRepository extends JpaRepository<CommandStateEntity, Long>{

    CommandStateEntity findByUserEntityId(Integer id);

}
