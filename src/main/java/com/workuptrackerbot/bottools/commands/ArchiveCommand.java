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
import java.util.*;

@BotCommand(command = "/archive")
public class ArchiveCommand extends Command {

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
        if (archiveProjects.isEmpty()) {
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

        sendMessage.setText(sb.toString());
        sendMessage.enableMarkdown(true);
        return sendMessage;
    }
}
