package com.workuptrackerbot.bottools.commands;

import com.workuptrackerbot.bottools.springbottools.annotations.Answer;
import com.workuptrackerbot.bottools.springbottools.annotations.BotCommand;
import com.workuptrackerbot.bottools.springbottools.commands.Command;
import com.workuptrackerbot.bottools.tlgmtools.ReplyKeyboardTools;
import com.workuptrackerbot.entity.Project;
import com.workuptrackerbot.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.text.MessageFormat;
import java.util.Properties;

@BotCommand(command="/leave_project")
public class LeaveProjectCommand extends Command {


    @Autowired
    private Properties properties;

    @Autowired
    private ProjectService projectService;

    @Answer(index = 0)
    public BotApiMethod nameQuestion(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChat().getId().toString());
        sendMessage.setText(properties.getProperty("command.leavecommand.enterNameProject"));
        return sendMessage;
    }

    @Answer(index = 1)
    public BotApiMethod leaveProject(Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChat().getId().toString());

        User user = message.getFrom();

        try {
            projectService.removeProject(user.getId(), message.getText());
            sendMessage.setText(
                    MessageFormat.format(
                            properties.getProperty("command.leavecommand.leavedProject"),
                            message.getText()));
            sendMessage.setReplyMarkup(
                    ReplyKeyboardTools.createReplyKeyboardMarkup(
                            projectService.getProjects(user.getId()),
                            Project::getName
                    ));
        } catch (Exception e) {
            sendMessage.setText(properties.getProperty("command.leavecommand.cantleave"));
        }
        return sendMessage;
    }





}
