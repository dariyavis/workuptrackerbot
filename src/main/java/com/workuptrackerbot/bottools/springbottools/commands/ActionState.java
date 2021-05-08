package com.workuptrackerbot.bottools.springbottools.commands;


import org.telegram.telegrambots.meta.api.objects.User;

public class ActionState {

    private User user;
    private String action;
    private String data;
    private boolean doForce = false;


    public ActionState(String action, String data, boolean doForce) {
        this.action = action;
        this.data = data;
        this.doForce = doForce;
    }

    public ActionState(String action, String data) {
        this.action = action;
        this.data = data;
    }

    public ActionState(String action, boolean doForce) {
        this.action = action;
        this.doForce = doForce;
    }

    public ActionState(User user, String action, String data) {
        this.user = user;
        this.action = action;
        this.data = data;
    }

    public ActionState(String action) {
        this.action = action;
    }

    public ActionState(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public boolean isDoForce() {
        return doForce;
    }

    public void setDoForce(boolean doForce) {
        this.doForce = doForce;
    }
}
