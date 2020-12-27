package com.workuptrackerbot.repository;

import com.workuptrackerbot.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>{

    public UserEntity findByUsername(String name);
}
