package com.workuptrackerbot.bottools.botstates.manage;

import com.workuptrackerbot.bottools.springbottools.annotations.BotAction;
import com.workuptrackerbot.bottools.springbottools.annotations.HasBotAction;
import com.workuptrackerbot.bottools.springbottools.commands.ActionState;
import com.workuptrackerbot.bottools.springbottools.commands.BotUpdate;
import com.workuptrackerbot.bottools.tlgmtools.MessageTools;
import com.workuptrackerbot.bottools.tlgmtools.ReplyKeyboardTools;
import com.workuptrackerbot.entity.Project;
import com.workuptrackerbot.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.text.MessageFormat;
import java.util.Properties;
import java.util.function.Consumer;

@HasBotAction
public class LeaveProject {

    @Autowired
    private Properties properties;

    @Autowired
    private ProjectService projectService;

    @BotAction(path = "leave", callback = true)
    public ActionState leaveProject(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {

        Update update = botUpdate.getUpdate();
        execute.accept(
                MessageTools.deleteMessage(update.getCallbackQuery().getMessage().getChatId().toString(),
                        update.getCallbackQuery().getMessage().getMessageId()));

        Message message = update.getCallbackQuery().getMessage();
        SendMessage sendMessage = MessageTools.createSendMessage(message.getChat().getId().toString());

        User user = update.getCallbackQuery().getFrom();
        String data = update.getCallbackQuery().getData();


        Project project = projectService.removeProjectById(user.getId(), data);
        sendMessage.setText(
                MessageFormat.format(
                        properties.getProperty("botstates.manage.leaveProject.text"),
                        project.getName()));
        sendMessage.setReplyMarkup(
                ReplyKeyboardTools.createReplyKeyboardMarkup(
                        projectService.getActiveProjects(user.getId()),
                        Project::getName
                ));

        execute.accept(sendMessage);
        return new ActionState("manage_project", user.getId().toString(), true);
    }
}
