package com.workuptrackerbot.repository;

import com.workuptrackerbot.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer>{

    UserEntity findByUsername(String username);

    UserEntity findByTlgId(Integer id);

    boolean existsByUsernameAndRegistrDateIsNotNull(String username);

}
