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
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.text.MessageFormat;
import java.util.Properties;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@HasBotAction
public class ArchiveProject {

    @Autowired
    private Properties properties;

    @Autowired
    private ProjectService projectService;

    @BotAction(path = "archive", callback = true)
    public ActionState archiveProject(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {
        return archiveProjectHandler(execute, botUpdate,
                properties.getProperty("botstates.manage.archive.archivedProject"),
                (userId, projectId) -> projectService.zipProjectById(userId, projectId));
    }

    @BotAction(path = "unarchive", callback = true)
    public ActionState unArchiveProject(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {
        return archiveProjectHandler(execute, botUpdate,
                properties.getProperty("botstates.manage.archive.unarchivedProject"),
                (userId, projectId) -> projectService.unzipProjectById(userId, projectId));
    }

    private ActionState archiveProjectHandler(Consumer<BotApiMethod> execute, BotUpdate botUpdate,
                                              String property, BiFunction<Integer, String, Project> callback) {

        Update update = botUpdate.getUpdate();
        execute.accept(
                MessageTools.deleteMessage(update.getCallbackQuery().getMessage().getChatId().toString(),
                        update.getCallbackQuery().getMessage().getMessageId()));
        User user = update.getCallbackQuery().getFrom();
        String projectId = update.getCallbackQuery().getData();
        Project project = callback.apply(user.getId(), projectId);

        SendMessage sendMessage = MessageTools.createSendMessage(user.getId().toString());
        sendMessage.setText(
                MessageFormat.format(property, project.getName()));
        sendMessage.setReplyMarkup(
                ReplyKeyboardTools.createReplyKeyboardMarkup(
                        projectService.getActiveProjects(user.getId()),
                        Project::getName));

        execute.accept(sendMessage);
        return new ActionState("selectActionForProject", projectId, true);
    }
}
