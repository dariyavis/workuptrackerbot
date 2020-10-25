package com.workuptrackerbot.bottools.commands;

import com.workuptrackerbot.bottools.springbottools.BotCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.lang.invoke.MethodHandles;
import java.util.Properties;

@BotCommand(command="/start")
public class StartCommand implements BotCommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private Properties properties;

    @Override
    public BotApiMethod handler(User user, Chat chat) {
        logger.info("User {} start work with bot", user.getUserName());
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId());
        message.setText(properties.getProperty("message.text.welcome"));
        return message;
    }


    //    @Override
//    public void execute(User user, Chat chat) {
//        logger.info("User {} start work with bot", user.getUserName());
//    }
}
