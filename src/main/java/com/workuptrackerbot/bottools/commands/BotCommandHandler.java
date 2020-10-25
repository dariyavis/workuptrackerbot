package com.workuptrackerbot.bottools.commands;

import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;

public interface BotCommandHandler {

    BotApiMethod handler(User user, Chat chat);
}
