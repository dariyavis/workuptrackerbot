package com.workuptrackerbot.data.service;

import com.workuptrackerbot.data.entity.Interval;
import com.workuptrackerbot.data.repository.IntervalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
