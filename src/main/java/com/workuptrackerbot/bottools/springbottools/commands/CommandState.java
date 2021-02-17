package com.workuptrackerbot.bottools.springbottools.commands;


import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.User;

public class CommandState {

    private BotApiMethod botApiMethod;
    private User user;
    private String command;
    private Integer index = -1;

    public CommandState(User user, String command) {
        this.user = user;
        this.command = command;
    }

    public CommandState(User user, String command, Integer index) {
        this.user = user;
        this.command = command;
        this.index = index;
    }

    public BotApiMethod getBotApiMethod() {
        return botApiMethod;
    }

    public void setBotApiMethod(BotApiMethod botApiMethod) {
        this.botApiMethod = botApiMethod;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
