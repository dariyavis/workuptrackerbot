package com.workuptrackerbot.bottools.springbottools.commands;


import org.telegram.telegrambots.meta.api.objects.User;

public class ActionState {

    private User user;
    private String action;

    public ActionState(User user, String action) {
        this.user = user;
        this.action = action;
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

}
