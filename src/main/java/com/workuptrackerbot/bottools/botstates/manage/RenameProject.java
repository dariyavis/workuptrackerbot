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
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.text.MessageFormat;
import java.util.Properties;
import java.util.function.Consumer;

@HasBotAction
public class RenameProject {

    @Autowired
    private Properties properties;

    @Autowired
    private ProjectService projectService;

    @BotAction(path = "renameProject", callback = true)
    public ActionState renameProjectQuestion(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {
        CallbackQuery callbackQuery = botUpdate.getUpdate().getCallbackQuery();
        Message message = callbackQuery.getMessage();

        User user = callbackQuery.getFrom();
        String projectId = callbackQuery.getData();

        execute.accept(
                MessageTools.deleteMessage(message.getChatId().toString(),
                        message.getMessageId()));

        execute.accept(
                MessageTools.createSendMessage(
                        message.getChat().getId().toString(),
                        properties.getProperty("botstates.manage.renameProject.question")));
        return new ActionState(user, "renameProjectHandler", projectId);
    }

    @BotAction(path = "renameProjectHandler")
    public ActionState renameProject(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {

        Message message = botUpdate.getUpdate().getMessage();
        SendMessage sendMessage = MessageTools.createSendMessage(message.getChat().getId().toString());

        User user = message.getFrom();

        try {
            Project project = projectService.renameProject(user.getId(), botUpdate.getActionData(), message.getText());
            sendMessage.setText(
                    MessageFormat.format(
                            properties.getProperty("botstates.manage.renameProject.text"),
                            project.getName()));
            sendMessage.setReplyMarkup(
                    ReplyKeyboardTools.createReplyKeyboardMarkup(
                            projectService.getActiveProjects(user.getId()),
                            Project::getName
                    ));
        } catch (Exception e) {
            sendMessage.setText(properties.getProperty("botstates.manage.projectExist"));
        }
        execute.accept(sendMessage);
        return new ActionState("selectActionForProject", botUpdate.getActionData(), true);
    }
}
