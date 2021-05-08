package com.workuptrackerbot.repository;

import com.workuptrackerbot.entity.ActionStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActionStateRepository extends JpaRepository<ActionStateEntity, Long>{

    ActionStateEntity findByUserEntity_TlgId(Integer id);

}
