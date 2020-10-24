package com.workuptrackerbot.main;

import java.io.Serializable;
import java.time.LocalDateTime;

public class DataTracker implements Serializable {

    private String user;
    private LocalDateTime startDate;
    private LocalDateTime stopDate;
    private String projectName;

    public DataTracker(String user, LocalDateTime startDate, LocalDateTime stopDate, String projectName) {
        this.user = user;
        this.startDate = startDate;
        this.stopDate = stopDate;
        this.projectName = projectName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getStopDate() {
        return stopDate;
    }

    public void setStopDate(LocalDateTime stopDate) {
        this.stopDate = stopDate;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
