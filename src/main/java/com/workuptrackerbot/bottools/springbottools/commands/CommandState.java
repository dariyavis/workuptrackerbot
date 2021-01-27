package com.workuptrackerbot.bottools.springbottools.commands;


import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;

public class CommandState {

    private BotApiMethod botApiMethod;
    private User user;
    private Chat chat;
    private String command;
    private Integer index = -1;

    public CommandState(User user, Chat chat, String command) {
        this.user = user;
        this.chat = chat;
        this.command = command;
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

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
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
