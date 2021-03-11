package com.workuptrackerbot.service;

import com.workuptrackerbot.entity.Interval;
import com.workuptrackerbot.entity.UserProject;
import com.workuptrackerbot.repository.IntervalRepository;
import com.workuptrackerbot.repository.UPRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

import java.sql.Date;
import java.sql.Timestamp;

@Service
public class IntervalService {

    @Autowired
    private UPRepository upRepository;

    @Autowired
    private IntervalRepository intervalRepository;

    public Interval createInterval(User user, String project_name, Timestamp startDate){

        UserProject up = upRepository.findByUserEntityIdAndProjectName(user.getId(), project_name);
        if(up == null || intervalRepository.findByUserProjectAndStopDateIsNull(up) != null) {
            return null;
        }

        Interval interval = new Interval(up, startDate);
        intervalRepository.save(interval);

        return interval;
//        intervalRepository.save(interval);
    }

    public Interval updateInterval(String interval_id, Timestamp stopDate) {
        Interval interval = intervalRepository.findById(Long.parseLong(interval_id)).orElseThrow();
        interval.setStopDate(stopDate);
        intervalRepository.save(interval);
        return interval;
    }

    public void deleteIntervals(UserProject up) {
        intervalRepository.deleteByUserProject(up);
    }

    public void removeInteval(String inteval_id) {
        intervalRepository.deleteById(Long.valueOf(inteval_id));
    }
}
