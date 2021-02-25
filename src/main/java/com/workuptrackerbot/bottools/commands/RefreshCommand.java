package com.workuptrackerbot.bottools.commands;

import com.workuptrackerbot.bottools.springbottools.annotations.Answer;
import com.workuptrackerbot.bottools.springbottools.commands.Command;
import com.workuptrackerbot.bottools.tlgmtools.ReplyKeyboardTools;
import com.workuptrackerbot.entity.Project;
import com.workuptrackerbot.entity.UserEntity;
import com.workuptrackerbot.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.lang.invoke.MethodHandles;
import java.util.Properties;

@com.workuptrackerbot.bottools.springbottools.annotations.BotCommand(command="/start")
public class RefreshCommand extends Command {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private Properties properties;

    @Autowired
    private UserService userService;


    @Answer(index = 0)
    public BotApiMethod handler(Message message) {
        logger.info("User {} refreshed", message.getFrom().getUserName());
        User user = message.getFrom();

        SendMessage messageNew = new SendMessage();
        messageNew.setChatId(message.getChatId().toString());

//        if(!userService.isUserExist(user)) {
//            userService.createOrUpdateUser(user, message.getChatId());
//            messageNew.setText(properties.getProperty("command.startcommand.welcome"));
//        }
//        else
//        {
            messageNew.setText(properties.getProperty("command.startcommand.refresh"));
            UserEntity userEntity = userService.createOrUpdateUser(user, message.getChatId());
            messageNew.setReplyMarkup(
                    ReplyKeyboardTools.createReplyKeyboardMarkup(userEntity.getProjects(), Project::getName));
//        }
        return messageNew;
    }
}
