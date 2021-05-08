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

import java.util.Properties;
import java.util.function.Consumer;

@HasBotAction
public class NewProject {


    @Autowired
    private Properties properties;

    @Autowired
    private ProjectService projectService;

    @BotAction(path = "addProject", callback = true)
    public ActionState addProjectQuestion(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {

        Update update = botUpdate.getUpdate();
        execute.accept(
                MessageTools.deleteMessage(update.getCallbackQuery().getMessage().getChatId().toString(),
                        update.getCallbackQuery().getMessage().getMessageId()));

        SendMessage sendMessage = MessageTools.createSendMessage(
                update.getCallbackQuery().getFrom().getId().toString(),
                properties.getProperty("botstates.manage.newproject.addProjectEnterQuestion"));
        execute.accept(sendMessage);
        return new ActionState("createProject");
    }

    @BotAction(path = "createProject")
    public ActionState createProject(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {

        Update update = botUpdate.getUpdate();
        Message message = update.getMessage();
        User user = message.getFrom();
        SendMessage sendMessage = MessageTools.createSendMessage(message.getChat().getId().toString());

        try {
            projectService.addProject(user.getId(), message.getText());
            sendMessage.setText(properties.getProperty("botstates.manage.newproject.addedProject") + " " + message.getText());
            sendMessage.setReplyMarkup(
                    ReplyKeyboardTools.createReplyKeyboardMarkup(
                            projectService.getActiveProjects(user.getId()),
                            Project::getName
                    ));
        } catch (Exception e) {
            sendMessage.setText(properties.getProperty("botstates.manage.projectExist"));
        }
        execute.accept(sendMessage);
        return new ActionState("manage_project", user.getId().toString(), true);
    }
}
