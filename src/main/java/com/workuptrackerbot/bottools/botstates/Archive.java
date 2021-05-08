package com.workuptrackerbot.bottools.botstates;

import com.workuptrackerbot.bottools.springbottools.annotations.BotAction;
import com.workuptrackerbot.bottools.springbottools.annotations.HasBotAction;
import com.workuptrackerbot.bottools.springbottools.commands.ActionState;
import com.workuptrackerbot.bottools.springbottools.commands.BotUpdate;
import com.workuptrackerbot.bottools.tlgmtools.MessageTools;
import com.workuptrackerbot.entity.Project;
import com.workuptrackerbot.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.MessageFormat;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;

@HasBotAction
public class Archive {

    @Autowired
    private Properties properties;

    @Autowired
    private ProjectService projectService;

    @BotAction(path = "archive", command = true)
    public ActionState showArchiveProjects(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {

        Update update = botUpdate.getUpdate();
        Message message = update.getMessage();

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

        execute.accept(
                MessageTools.createSendMessage(
                        message.getChat().getId().toString(), sb.toString()));
        return null;
    }
}
