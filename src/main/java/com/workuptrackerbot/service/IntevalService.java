package com.workuptrackerbot.service;

import com.workuptrackerbot.entity.Interval;
import com.workuptrackerbot.repository.IntervalRepository;
import org.springframework.beans.factory.annotation.Autowired;

//@Service
public class IntevalService {

    @Autowired
    private IntervalRepository intervalRepository;

//    public IntevalService(IntervalRepository intevalRepository){
//        this.intervalRepository = intevalRepository;
//    }

    public void createInterval(Interval interval){
//        intervalRepository.save(interval);
    }
}
