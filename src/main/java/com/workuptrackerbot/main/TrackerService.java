package com.workuptrackerbot.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Deprecated
@Service
public class TrackerService {

    private Map<String, DataTracker> map = new HashMap<>();;

    private static final Logger logger = LoggerFactory.getLogger(TrackerService.class.getName());

    public void startLog(String user, Date date) {

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        logger.info("User {} start logging time at {}", user, dateFormat.format(date));

        map.put(user, new DataTracker(user,LocalDateTime.now(), null,null));
    }

    public String stopLog(String user, Date date) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        DataTracker tracker = map.get(user);
        tracker.setStopDate(LocalDateTime.now());

        logger.info("User " + user + " stop logging time at " + map.get(user).getStopDate().format(formatter));

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("System logged time: ");
        stringBuilder.append(ChronoUnit.HOURS.between(
                map.get(user).getStartDate(), map.get(user).getStopDate()));
        stringBuilder.append(":");
        stringBuilder.append(ChronoUnit.MINUTES.between(
                map.get(user).getStartDate(), map.get(user).getStopDate())%60);
        stringBuilder.append(":");
        stringBuilder.append(ChronoUnit.SECONDS.between(
                map.get(user).getStartDate(), map.get(user).getStopDate())%60);
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }
}
