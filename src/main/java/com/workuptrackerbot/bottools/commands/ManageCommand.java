package com.workuptrackerbot.bottools.commands;

import com.workuptrackerbot.bottools.springbottools.annotations.*;
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
    public String nameQuestion(Consumer<BotApiMethod> execute, Update update) {
        Message message = update.getMessage();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChat().getId().toString());
        sendMessage.setText(properties.getProperty("command.managecommand.selectProject"));
        List<Project> projects = projectService.getProjects(message.getFrom().getId());

        sendMessage.setReplyMarkup(
                ReplyKeyboardTools.createInlineKeyboardMarkupWithPath(
                projects.stream().map(project -> new ReplyKeyboardTools.ButtonWithPath(
                        project.getName(),
                        "selectAction",
                        project.getId().toString())).collect(Collectors.toList()), 3
        ));
        execute.accept(sendMessage);
        return "selectAction";
    }

    @BotAction(path = "selectAction", callback = true)
    public String selectAction(Consumer<BotApiMethod> execute, Update update){

        execute.accept(
                MessageTools.deleteMessage(update.getCallbackQuery().getMessage().getChatId().toString(),
                update.getCallbackQuery().getMessage().getMessageId()));

        String projectId = update.getCallbackQuery().getData();
        UserProject up = projectService.getProjectInfoById(update.getCallbackQuery().getFrom().getId(), projectId);

        SendMessage sendMessage = createSendMessage(update.getCallbackQuery().getMessage().getChat().getId().toString());
        sendMessage.setText(MessageFormat.format(
                properties.getProperty("command.managecommand.selectaction"),
                up.getProject().getName()));

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttonsLineProjectAction = new ArrayList<>();
        if(up.isActive()){
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
//        buttonsLineProjectAction.add(ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
//                properties.getProperty("command.managecommand.rename"),
//                "/manage_projectrename", projectId));

        List<InlineKeyboardButton> buttonsLineExitAction = new ArrayList<>();
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
    public String leaveProject(Consumer<BotApiMethod> execute, Update update) {
        Message message = update.getCallbackQuery().getMessage();
        SendMessage sendMessage = createSendMessage(message.getChat().getId().toString());

        User user = update.getCallbackQuery().getFrom();
        String data = update.getCallbackQuery().getData();

        try {
            projectService.removeProjectById(user.getId(), data);
            sendMessage.setText(
                    MessageFormat.format(
                            properties.getProperty("command.managecommand.leavedProject"),
                            data));
            sendMessage.setReplyMarkup(
                    ReplyKeyboardTools.createReplyKeyboardMarkup(
                            projectService.getActiveProjects(user.getId()),
                            Project::getName
                    ));
        } catch (Exception e) {
            execute.accept(fillErrorMessage(message));
        }
        execute.accept(sendMessage);
        return null;
    }

    @BotAction(path = "archive", callback = true)
    public String archiveProject(Consumer<BotApiMethod> execute, Update update) {
        Message message = update.getCallbackQuery().getMessage();
        SendMessage sendMessage = createSendMessage(message.getChat().getId().toString());

        User user = update.getCallbackQuery().getFrom();
        String data = update.getCallbackQuery().getData();

        try {
            projectService.zipProjectById(user.getId(), data);
            sendMessage.setText(
                    MessageFormat.format(
                            properties.getProperty("command.managecommand.archivedProject"),
                            data));
            sendMessage.setReplyMarkup(
                    ReplyKeyboardTools.createReplyKeyboardMarkup(
                            projectService.getActiveProjects(user.getId()),
                            Project::getName
                    ));
        } catch (Exception e) {
            execute.accept(fillErrorMessage(message));
        }
        execute.accept(sendMessage);
        return null;
    }

    @BotAction(path = "unarchive", callback = true)
    public String unArchiveProject(Consumer<BotApiMethod> execute, Update update) {
        Message message = update.getCallbackQuery().getMessage();
        SendMessage sendMessage = createSendMessage(message.getChat().getId().toString());

        User user = update.getCallbackQuery().getFrom();
        String data = update.getCallbackQuery().getData();

        try {
            projectService.unzipProjectById(user.getId(), data);
            sendMessage.setText(
                    MessageFormat.format(
                            properties.getProperty("command.managecommand.unarchivedProject"),
                            data));
            sendMessage.setReplyMarkup(
                    ReplyKeyboardTools.createReplyKeyboardMarkup(
                            projectService.getActiveProjects(user.getId()),
                            Project::getName
                    ));
        } catch (Exception e) {
            execute.accept(fillErrorMessage(message));
        }
        execute.accept(sendMessage);
        return null;
    }

    @BotAction(path = "exit", callback = true)
    public String exit(Consumer<BotApiMethod> execute, Update update) {
        Message message = update.getCallbackQuery().getMessage();
        SendMessage sendMessage = createSendMessage(message.getChat().getId().toString());

        User user = update.getCallbackQuery().getFrom();

        try {
            sendMessage.setText(properties.getProperty("command.managecommand.exit"));
            sendMessage.setReplyMarkup(
                    ReplyKeyboardTools.createReplyKeyboardMarkup(
                            projectService.getActiveProjects(user.getId()),
                            Project::getName
                    ));
        } catch (Exception e) {
            sendMessage.setText(properties.getProperty("command.managecommand.error"));
        }
        execute.accept(sendMessage);
        return null;
    }

    private SendMessage createSendMessage(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableMarkdown(true);
        return sendMessage;
    }


    private BotApiMethod fillErrorMessage(Message message) {
        SendMessage sendMessage = createSendMessage(message.getChat().getId().toString());
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
