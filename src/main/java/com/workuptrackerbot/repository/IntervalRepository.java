package com.workuptrackerbot.repository;

import com.workuptrackerbot.entity.Interval;
import com.workuptrackerbot.entity.UserProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntervalRepository  extends JpaRepository<Interval, Long> {

    void deleteByUserProject(UserProject up);

}
