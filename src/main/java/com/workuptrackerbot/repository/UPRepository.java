package com.workuptrackerbot.repository;

import com.workuptrackerbot.entity.Project;
import com.workuptrackerbot.entity.UserEntity;
import com.workuptrackerbot.entity.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UPRepository extends JpaRepository<UserProject, Long>{

    List<UserProject> findByUserEntity(UserEntity user);


//    @Query("select c from Child c join fetch c.parent where c.id = :id")
//    UserProject findByIdFetchParent(@Param("id") Long id);

    UserProject findByUserEntityIdAndProjectName(Integer user_id, String project_name);
}
