package com.workuptrackerbot.bottools.commands;

import com.workuptrackerbot.bottools.springbottools.annotations.Answer;
import com.workuptrackerbot.bottools.springbottools.annotations.BotCommand;
import com.workuptrackerbot.bottools.springbottools.commands.Command;
import com.workuptrackerbot.bottools.tlgmtools.ReplyKeyboardTools;
import com.workuptrackerbot.entity.Project;
import com.workuptrackerbot.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;

@BotCommand(command="/archive")
public class ArchiveCommand extends Command {

    public enum ArchiveAction {
        YES("yes"),
        NO("no");

        private  String action;

        ArchiveAction(String action) {
            this.action = action;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String command) {
            this.action = command;
        }
    }


    @Autowired
    private Properties properties;

    @Autowired
    private ProjectService projectService;

    @Answer(index = 0)
    public BotApiMethod showArchiveProjects(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChat().getId().toString());
        StringBuilder sb = new StringBuilder();
        sb.append(MessageFormat.format(
                properties.getProperty("command.archive.archiveProjectsTitle"),
                message.getText()));
        List<Project> archiveProjects = projectService.getArchiveProjects(message.getFrom().getId());
        if(archiveProjects.isEmpty()) {
            sb.append(MessageFormat.format(
                    properties.getProperty("command.archive.archiveProjectsEmpty"),
                    message.getText()));
        } else {
            archiveProjects.forEach(project -> {
                sb.append(MessageFormat.format(
                        properties.getProperty("command.archive.archiveProject"),
                        project.getName()));
            });
        }
        sb.append(properties.getProperty("command.archive.enterTypeAction"));

        sendMessage.setText(sb.toString());
//        sendMessage.setReplyMarkup(
//                ReplyKeyboardTools.removeKeyBord());
        sendMessage.setReplyMarkup(
                ReplyKeyboardTools.createReplyKeyboardMarkup(
                        Arrays.asList(ArchiveAction.values()),
//                        ArchiveAction::getAction
                        this::getArchiveActionName
                ));
        sendMessage.enableMarkdown(true);
        return sendMessage;
    }

    @Answer(index = 1)
    public BotApiMethod selectAction(Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChat().getId().toString());

        User user = message.getFrom();

        ArchiveAction result = getArchiveActionByName(message.getText());
        switch (result) {
            case YES: {
                List<Project> archiveProjects = projectService.getArchiveProjects(user.getId());
                sendMessage.setText(properties.getProperty("command.archive.selectProject"));
                sendMessage.setReplyMarkup(
                        ReplyKeyboardTools.createReplyKeyboardMarkup(archiveProjects,Project::getName));
                break;
            }
            case NO: {
                exitCommand(user, sendMessage, properties.getProperty("command.archive.exitCommand"));
                stop();
                break;
            }
        }
        return sendMessage;
    }

    private String getArchiveActionName(ArchiveAction archiveAction){
        return properties.getProperty("command.archive." + archiveAction.getAction());
    }

    private ArchiveAction getArchiveActionByName(String name) {
        for (ArchiveAction action : ArchiveAction.values()) {
            if(name.equals(getArchiveActionName(action))) {
                return action;
            }
        }
        return null;
    }

    @Answer(index = 2)
    public BotApiMethod zipProject(Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChat().getId().toString());

        User user = message.getFrom();

        projectService.zipProjectByName(user.getId(), message.getText());
        exitCommand(user, sendMessage, properties.getProperty("command.archive.unzipProject"));
        return sendMessage;
    }

    private void exitCommand(User user, SendMessage sendMessage, String text) {
        List<Project> activeProjects = projectService.getActiveProjects(user.getId());
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(
                ReplyKeyboardTools.createReplyKeyboardMarkup(activeProjects,Project::getName));
    }




}
