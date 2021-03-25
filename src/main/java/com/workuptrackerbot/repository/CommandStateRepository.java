package com.workuptrackerbot.repository;

import com.workuptrackerbot.entity.ActionStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandStateRepository extends JpaRepository<ActionStateEntity, Long>{

    ActionStateEntity findByUserEntityId(Integer id);

}
