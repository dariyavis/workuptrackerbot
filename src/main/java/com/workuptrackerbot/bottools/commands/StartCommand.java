package com.workuptrackerbot.bottools.commands;

import com.workuptrackerbot.bottools.springbottools.annotations.Answer;
import com.workuptrackerbot.entity.UserEntity;
import com.workuptrackerbot.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;

import java.lang.invoke.MethodHandles;
import java.util.Properties;

@com.workuptrackerbot.bottools.springbottools.annotations.BotCommand(command="/start")
public class StartCommand extends Command {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private Properties properties;

    @Autowired
    private UserService userService;

    @Answer(index = 0)
    public BotApiMethod handler(Message message) {
        logger.info("User {} start work with bot", message.getFrom().getUserName());
        User user = message.getFrom();

        SendMessage messageNew = new SendMessage();
        messageNew.setChatId(message.getChatId());

        if(!userService.isUserExist(user)) {
            messageNew.setText(properties.getProperty("command.startcommand.welcome"));
        }
        else
        {
            messageNew.setText(properties.getProperty("command.startcommand.reset"));
        }
        return messageNew;
    }

}
