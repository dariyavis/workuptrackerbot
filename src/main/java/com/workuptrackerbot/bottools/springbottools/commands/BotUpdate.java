package com.workuptrackerbot.bottools.springbottools.commands;

import org.telegram.telegrambots.meta.api.objects.Update;

public class BotUpdate {
    private String actionData;
    private Update update;

    public BotUpdate(Update update, String actionData) {
        this.actionData = actionData;
        this.update = update;
    }

    public BotUpdate(Update update) {
        this.update = update;
    }

    public Update getUpdate() {
        return update;
    }

    public void setUpdate(Update update) {
        this.update = update;
    }

    public String getActionData() {
        return actionData;
    }

    public void setActionData(String actionData) {
        this.actionData = actionData;
    }
}
