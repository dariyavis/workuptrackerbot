package com.workuptrackerbot.bottools.springbottools.commands.commandsdepricated;

public enum Command {
    START("/start"),
    NEW_PROJECT("/new_project"),
    ADD_USER_TO_PROJECT("/add_user_to_project"),
    LEAVE_PROJECT("/leave_project");
//    SETTINGS("settings");
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
