package com.workuptrackerbot.bottools.commands;

import com.workuptrackerbot.bottools.springbottools.annotations.BotCommand;
import com.workuptrackerbot.bottools.tlgmtools.ReplyKeyboardTools;
import com.workuptrackerbot.bottools.springbottools.annotations.Answer;
import com.workuptrackerbot.bottools.springbottools.commands.Command;
import com.workuptrackerbot.entity.Interval;
import com.workuptrackerbot.entity.Project;
import com.workuptrackerbot.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Collections;
import java.util.Properties;
import java.util.function.Function;

@BotCommand(command="/new_project")
public class NewProjectCommand extends Command {


    @Autowired
    private Properties properties;

    @Autowired
    private ProjectService projectService;

    @Answer(index = 0)
    public BotApiMethod nameQuestion(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChat().getId().toString());
        sendMessage.setText(properties.getProperty("command.createcommand.enterNameProject"));
        return sendMessage;
    }

    @Answer(index = 1)
    public BotApiMethod addProject(Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChat().getId().toString());

        User user = message.getFrom();

        try {
            projectService.addProject(user.getId(), message.getText());
            sendMessage.setText(properties.getProperty("command.createcommand.addedProject") + " " + message.getText());
            sendMessage.setReplyMarkup(
                    ReplyKeyboardTools.createReplyKeyboardMarkup(
                            projectService.getProjects(user.getId()),
                            Project::getName
                    ));
        } catch (Exception e) {
            sendMessage.setText(properties.getProperty("command.createcommand.projectexist"));
        }
        return sendMessage;
    }





}
