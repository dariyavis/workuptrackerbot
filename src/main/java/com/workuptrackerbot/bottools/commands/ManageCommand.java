package com.workuptrackerbot.bottools.commands;

import com.workuptrackerbot.bottools.springbottools.annotations.*;
import com.workuptrackerbot.bottools.springbottools.commands.ActionState;
import com.workuptrackerbot.bottools.springbottools.commands.BotUpdate;
import com.workuptrackerbot.bottools.tlgmtools.MessageTools;
import com.workuptrackerbot.bottools.tlgmtools.ReplyKeyboardTools;
import com.workuptrackerbot.entity.Project;
import com.workuptrackerbot.entity.UserProject;
import com.workuptrackerbot.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@HasBotAction
public class ManageCommand {

    @Autowired
    private Properties properties;

    @Autowired
    private ProjectService projectService;

    @BotAction(path = "manage_project", command = true, callback = true)
    public ActionState nameQuestion(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {

        Update update = botUpdate.getUpdate();
        if (update.getCallbackQuery() != null) {
            execute.accept(
                    MessageTools.deleteMessage(update.getCallbackQuery().getMessage().getChatId().toString(),
                            update.getCallbackQuery().getMessage().getMessageId()));
        }

        Message message = update.getCallbackQuery() == null ? update.getMessage() : update.getCallbackQuery().getMessage();
        User user = update.getCallbackQuery() == null ? update.getMessage().getFrom() : update.getCallbackQuery().getFrom();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChat().getId().toString());
        sendMessage.setText(properties.getProperty("command.managecommand.selectProject"));
        List<Project> projects = projectService.getProjects(user.getId());

        sendMessage.setReplyMarkup(
                ReplyKeyboardTools.createInlineKeyboardMarkupWithPath(
                        projects.stream().map(project -> new ReplyKeyboardTools.ButtonWithPath(
                                project.getName(),
                                "selectAction",
                                project.getId().toString())).collect(Collectors.toList()), 3
                ));
        execute.accept(sendMessage);
        return new ActionState(message.getFrom(), "selectAction");
    }

    @BotAction(path = "selectAction", callback = true)
    public ActionState selectAction(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {

        Update update = botUpdate.getUpdate();

        Message message = update.getCallbackQuery() == null ? update.getMessage() : update.getCallbackQuery().getMessage();
        User user = update.getCallbackQuery() == null ? update.getMessage().getFrom() : update.getCallbackQuery().getFrom();


        if(message.getMessageId()!= null) {
            execute.accept(
                    MessageTools.deleteMessage(message.getChatId().toString(),
                            message.getMessageId()));
        }

        String projectId = update.getCallbackQuery() == null ? botUpdate.getActionData() : update.getCallbackQuery().getData();
        UserProject up = projectService.getProjectInfoById(user.getId(), projectId);

        SendMessage sendMessage = MessageTools.createSendMessage(message.getChat().getId().toString());
        sendMessage.setText(MessageFormat.format(
                properties.getProperty("command.managecommand.selectaction"),
                up.getProject().getName()));

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttonsLineProjectAction = new ArrayList<>();
        if (up.isActive()) {
            buttonsLineProjectAction.add(ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
                    properties.getProperty("command.managecommand.archive"),
                    "archive", projectId));
        } else {
            buttonsLineProjectAction.add(ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
                    properties.getProperty("command.managecommand.unarchive"),
                    "unarchive", projectId));
        }
        buttonsLineProjectAction.add(ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
                properties.getProperty("command.managecommand.leave"),
                "leave", projectId));
        buttonsLineProjectAction.add(ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
                properties.getProperty("command.managecommand.rename"),
                "renameProjectQuestion", projectId));

        List<InlineKeyboardButton> buttonsLineExitAction = new ArrayList<>();
        buttonsLineExitAction.add(ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
                properties.getProperty("command.managecommand.back"),
                "manage_project", projectId));
        buttonsLineExitAction.add(ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
                properties.getProperty("command.managecommand.exit"),
                "exit", projectId));

        buttons.add(buttonsLineProjectAction);
        buttons.add(buttonsLineExitAction);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        execute.accept(sendMessage);
        return null;
    }

    @BotAction(path = "leave", callback = true)
    public ActionState leaveProject(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {

        Update update = botUpdate.getUpdate();
        Message message = update.getCallbackQuery().getMessage();
        SendMessage sendMessage = MessageTools.createSendMessage(message.getChat().getId().toString());

        User user = update.getCallbackQuery().getFrom();
        String data = update.getCallbackQuery().getData();

        try {
            Project project = projectService.removeProjectById(user.getId(), data);
            sendMessage.setText(
                    MessageFormat.format(
                            properties.getProperty("command.managecommand.leavedProject"),
                            project.getName()));
            sendMessage.setReplyMarkup(
                    ReplyKeyboardTools.createReplyKeyboardMarkup(
                            projectService.getActiveProjects(user.getId()),
                            Project::getName
                    ));
        } catch (Exception e) {
            execute.accept(fillErrorMessage(message));
        }
        execute.accept(sendMessage);
        nameQuestion(execute, botUpdate);
        return null;
    }

    @BotAction(path = "archive", callback = true)
    public ActionState archiveProject(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {

        Update update = botUpdate.getUpdate();
        Message message = update.getCallbackQuery().getMessage();
        SendMessage sendMessage = MessageTools.createSendMessage(message.getChat().getId().toString());

        User user = update.getCallbackQuery().getFrom();
        String data = update.getCallbackQuery().getData();

        try {
            Project project = projectService.zipProjectById(user.getId(), data);
            sendMessage.setText(
                    MessageFormat.format(
                            properties.getProperty("command.managecommand.archivedProject"),
                            project.getName()));
            sendMessage.setReplyMarkup(
                    ReplyKeyboardTools.createReplyKeyboardMarkup(
                            projectService.getActiveProjects(user.getId()),
                            Project::getName
                    ));
        } catch (Exception e) {
            execute.accept(fillErrorMessage(message));
        }
        execute.accept(sendMessage);
        selectAction(execute, botUpdate);
        return null;
    }

    @BotAction(path = "unarchive", callback = true)
    public ActionState unArchiveProject(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {

        Update update = botUpdate.getUpdate();
        Message message = update.getCallbackQuery().getMessage();
        SendMessage sendMessage = MessageTools.createSendMessage(message.getChat().getId().toString());

        User user = update.getCallbackQuery().getFrom();
        String data = update.getCallbackQuery().getData();

        try {
            Project project = projectService.unzipProjectById(user.getId(), data);
            sendMessage.setText(
                    MessageFormat.format(
                            properties.getProperty("command.managecommand.unarchivedProject"),
                            project.getName()));
            sendMessage.setReplyMarkup(
                    ReplyKeyboardTools.createReplyKeyboardMarkup(
                            projectService.getActiveProjects(user.getId()),
                            Project::getName
                    ));
        } catch (Exception e) {
            execute.accept(fillErrorMessage(message));
        }
        execute.accept(sendMessage);
        selectAction(execute, botUpdate);
        return null;
    }

    @BotAction(path = "renameProjectQuestion", callback = true)
    public ActionState renameProjectQuestion(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {
        Update update = botUpdate.getUpdate();
        Message message = update.getCallbackQuery().getMessage();
        SendMessage sendMessage = MessageTools.createSendMessage(message.getChat().getId().toString());

        User user = update.getCallbackQuery().getFrom();
        String data = update.getCallbackQuery().getData();

        execute.accept(
                MessageTools.deleteMessage(message.getChatId().toString(),
                        message.getMessageId()));

        sendMessage.setText(properties.getProperty("command.managecommand.renameProjectQuestion"));
        execute.accept(sendMessage);
        return new ActionState(user, "renameProject", data);
    }

    @BotAction(path = "renameProject")
    public ActionState renameProject(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {
        Update update = botUpdate.getUpdate();
        Message message = botUpdate.getUpdate().getMessage();
        SendMessage sendMessage = MessageTools.createSendMessage(message.getChat().getId().toString());

        User user = update.getMessage().getFrom();

        Project project = projectService.renameProject(botUpdate.getActionData(), message.getText());
        if (project == null) {
            execute.accept(fillErrorMessage(message));
            exit(execute, botUpdate);
            return null;
        }
        sendMessage.setText(
                MessageFormat.format(
                        properties.getProperty("command.managecommand.renameProject"),
                        project.getName()));
        sendMessage.setReplyMarkup(
                ReplyKeyboardTools.createReplyKeyboardMarkup(
                        projectService.getActiveProjects(user.getId()),
                        Project::getName
                ));
        execute.accept(sendMessage);
        botUpdate.getUpdate().getMessage().setMessageId(null);
        return selectAction(execute, botUpdate);
    }

    @BotAction(path = "exit", callback = true)
    public ActionState exit(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {

        Update update = botUpdate.getUpdate();
        Message message = update.getCallbackQuery() == null ? update.getMessage() : update.getCallbackQuery().getMessage();


        execute.accept(
                MessageTools.deleteMessage(message.getChatId().toString(),
                        message.getMessageId()));
        return null;
    }


    private BotApiMethod fillErrorMessage(Message message) {
        SendMessage sendMessage = MessageTools.createSendMessage(message.getChat().getId().toString());
        sendMessage.setText(properties.getProperty("command.managecommand.error"));
        sendMessage.setReplyMarkup(
                ReplyKeyboardTools.createReplyKeyboardMarkup(
                        projectService.getActiveProjects(message.getFrom().getId()),
                        Project::getName
                ));
        return sendMessage;
    }

//    private String createData(String path, String data) {
//        return new JSONObject().put("data", data).put("path", path).toString();
//    }

}
