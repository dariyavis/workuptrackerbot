package com.workuptrackerbot.bottools.commands;

import com.workuptrackerbot.bottools.springbottools.annotations.Answer;
import com.workuptrackerbot.bottools.springbottools.annotations.BotCommand;
import com.workuptrackerbot.bottools.springbottools.annotations.CallbackQueryHandler;
import com.workuptrackerbot.bottools.springbottools.annotations.HasCallbackQuery;
import com.workuptrackerbot.bottools.springbottools.commands.Command;
import com.workuptrackerbot.bottools.tlgmtools.ReplyKeyboardTools;
import com.workuptrackerbot.entity.Project;
import com.workuptrackerbot.entity.UserProject;
import com.workuptrackerbot.service.ProjectService;
import org.json.JSONObject;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

@BotCommand(command = "/manage_project")
@HasCallbackQuery(prefix = "/manage_project")
public class ManageCommand extends Command {

    @Autowired
    private Properties properties;

    @Autowired
    private ProjectService projectService;

    @Answer(index = 0)
    public BotApiMethod nameQuestion(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChat().getId().toString());
        sendMessage.setText(properties.getProperty("command.managecommand.enterNameProject"));
        sendMessage.setReplyMarkup(
                ReplyKeyboardTools.createReplyKeyboardMarkup(
                        projectService.getProjects(message.getFrom().getId()),
                        Project::getName
                ));
        return sendMessage;
    }

    @Answer(index = 1)
    public BotApiMethod selectAction(Message message){
        UserProject up = projectService.getProjectInfo(message.getFrom().getId(), message.getText());
        if(up == null){
            return fillErrorMessage(message);
        }

        SendMessage sendMessage = createSendMessage(message.getChat().getId().toString());
        sendMessage.setText(MessageFormat.format(
                properties.getProperty("command.managecommand.selectaction"),
                message.getText()));

//        List<ReplyKeyboardTools.ButtonWithPath> buttons = new LinkedList<>()

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttonsLineProjectAction = new ArrayList<>();
        if(up.isActive()){
            buttonsLineProjectAction.add(ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
                    properties.getProperty("command.managecommand.archive"),
                    "/manage_projectarchive", message.getText()));
        } else {
            buttonsLineProjectAction.add(ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
                    properties.getProperty("command.managecommand.unarchive"),
                    "/manage_projectunarchive", message.getText()));
        }
        buttonsLineProjectAction.add(ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
                properties.getProperty("command.managecommand.leave"),
                "/manage_projectleave", message.getText()));
//        buttonsLineProjectAction.add(ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
//                properties.getProperty("command.managecommand.rename"),
//                "/manage_projectrename", message.getText()));

        List<InlineKeyboardButton> buttonsLineExitAction = new ArrayList<>();
        buttonsLineExitAction.add(ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
                properties.getProperty("command.managecommand.exit"),
                "/manage_projectexit", message.getText()));

        buttons.add(buttonsLineProjectAction);
        buttons.add(buttonsLineExitAction);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        return sendMessage;
    }

    @CallbackQueryHandler(path = "leave")
    public BotApiMethod leaveProject(Update update) {
        Message message = update.getCallbackQuery().getMessage();
        SendMessage sendMessage = createSendMessage(message.getChat().getId().toString());

        User user = update.getCallbackQuery().getFrom();
        String data = update.getCallbackQuery().getData();

        try {
            projectService.removeProject(user.getId(), data);
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
            return fillErrorMessage(message);
        }
        return sendMessage;
    }

    @CallbackQueryHandler(path = "archive")
    public BotApiMethod archiveProject(Update update) {
        Message message = update.getCallbackQuery().getMessage();
        SendMessage sendMessage = createSendMessage(message.getChat().getId().toString());

        User user = update.getCallbackQuery().getFrom();
        String data = update.getCallbackQuery().getData();

        try {
            projectService.zipProjectByName(user.getId(), data);
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
            return fillErrorMessage(message);
        }
        return sendMessage;
    }

    @CallbackQueryHandler(path = "unarchive")
    public BotApiMethod unArchiveProject(Update update) {
        Message message = update.getCallbackQuery().getMessage();
        SendMessage sendMessage = createSendMessage(message.getChat().getId().toString());

        User user = update.getCallbackQuery().getFrom();
        String data = update.getCallbackQuery().getData();

        try {
            projectService.unzipProjectByName(user.getId(), data);
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
            return fillErrorMessage(message);
        }
        return sendMessage;
    }

    @CallbackQueryHandler(path = "exit")
    public BotApiMethod exit(Update update) {
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
        return sendMessage;
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

    private String createData(String path, String data) {
        return new JSONObject().put("data", data).put("path", path).toString();
    }

}
