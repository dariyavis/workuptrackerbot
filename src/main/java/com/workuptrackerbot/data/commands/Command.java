package com.workuptrackerbot.data.commands;

public enum Command {
    START("/start"),
    SETTINGS("settings");
//list projects
    private  String command;

    Command(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
