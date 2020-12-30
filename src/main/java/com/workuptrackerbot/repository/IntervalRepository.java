package com.workuptrackerbot.repository;

import com.workuptrackerbot.entity.Interval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IntervalRepository  extends JpaRepository<Interval, Long> {



}
