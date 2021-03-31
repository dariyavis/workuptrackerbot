package com.workuptrackerbot.bottools.commands;

import com.workuptrackerbot.bottools.springbottools.annotations.BotAction;
import com.workuptrackerbot.bottools.springbottools.annotations.HasBotAction;
import com.workuptrackerbot.bottools.springbottools.commands.ActionState;
import com.workuptrackerbot.bottools.springbottools.commands.BotUpdate;
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
public class NewProjectCommand {


    @Autowired
    private Properties properties;

    @Autowired
    private ProjectService projectService;

    @BotAction(path = "new_project", command = true)
    public ActionState nameQuestion(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {

        Update update = botUpdate.getUpdate();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getMessage().getChat().getId().toString());
        sendMessage.setText(properties.getProperty("command.createcommand.enterNameProject"));
        execute.accept(sendMessage);
        return new ActionState(update.getMessage().getFrom(),"addProject");
    }

    @BotAction(path = "addProject")
    public ActionState addProject(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {

        Update update = botUpdate.getUpdate();
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChat().getId().toString());

        User user = message.getFrom();

        try {
            projectService.addProject(user.getId(), message.getText());
            sendMessage.setText(properties.getProperty("command.createcommand.addedProject") + " " + message.getText());
            sendMessage.setReplyMarkup(
                    ReplyKeyboardTools.createReplyKeyboardMarkup(
                            projectService.getActiveProjects(user.getId()),
                            Project::getName
                    ));
        } catch (Exception e) {
            sendMessage.setText(properties.getProperty("command.createcommand.projectexist"));
        }
        execute.accept(sendMessage);
        return null;
    }
}
