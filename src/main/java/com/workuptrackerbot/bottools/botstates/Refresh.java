package com.workuptrackerbot.bottools.botstates;

import com.workuptrackerbot.bottools.springbottools.annotations.BotAction;
import com.workuptrackerbot.bottools.springbottools.annotations.HasBotAction;
import com.workuptrackerbot.bottools.springbottools.commands.ActionState;
import com.workuptrackerbot.bottools.springbottools.commands.BotUpdate;
import com.workuptrackerbot.bottools.tlgmtools.MessageTools;
import com.workuptrackerbot.bottools.tlgmtools.ReplyKeyboardTools;
import com.workuptrackerbot.entity.Project;
import com.workuptrackerbot.service.ProjectService;
import com.workuptrackerbot.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.lang.invoke.MethodHandles;
import java.util.Properties;
import java.util.function.Consumer;

@HasBotAction
public class Refresh {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Autowired
    private Properties properties;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @BotAction(path = "refresh", command = true)
    public ActionState refresh(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {

        Update update = botUpdate.getUpdate();
        Message message = update.getMessage();
        logger.info("User {} refreshed", message.getFrom().getUserName());
        User user = message.getFrom();

        SendMessage messageNew = MessageTools.createSendMessage(message.getChat().getId().toString());
        messageNew.setText(properties.getProperty("botstates.refresh"));
        userService.createOrUpdateUser(user);

        messageNew.setReplyMarkup(
                ReplyKeyboardTools.createReplyKeyboardMarkup(
                        projectService.getActiveProjects(user.getId()),
                        Project::getName));
        execute.accept(messageNew);
        return null;
    }
}
