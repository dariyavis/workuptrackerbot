package com.workuptrackerbot.bottools.botstates.manage;

import com.workuptrackerbot.bottools.springbottools.annotations.BotAction;
import com.workuptrackerbot.bottools.springbottools.annotations.HasBotAction;
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
import java.util.function.Function;
import java.util.stream.Collectors;

@HasBotAction
public class Manage {

    @Autowired
    private Properties properties;

    @Autowired
    private ProjectService projectService;

    @BotAction(path = "manage_project", command = true)
    public ActionState manageCommand(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {
        Update update = botUpdate.getUpdate();
        return manage(execute, update.getMessage().getFrom().getId());
    }

    @BotAction(path = "manage_project", callback = true)
    public ActionState manageCallback(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {
        Update update = botUpdate.getUpdate();
        execute.accept(
                MessageTools.deleteMessage(update.getCallbackQuery().getMessage().getChatId().toString(),
                        update.getCallbackQuery().getMessage().getMessageId()));
        return manage(execute, update.getCallbackQuery().getFrom().getId());
    }

    @BotAction(path = "manage_project")
    public ActionState manageState(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {
        return manage(execute, Integer.valueOf(botUpdate.getActionData()));
    }

    private ActionState manage(Consumer<BotApiMethod> execute, Integer userId) {

        SendMessage sendMessage = MessageTools.createSendMessage(userId.toString(),
                properties.getProperty("botstates.manage.manage.text"));
        List<UserProject> projects = projectService.getUPs(userId);

        InlineKeyboardMarkup keyboardMarkup = ReplyKeyboardTools.createInlineKeyboardMarkupWithPath(
                projects.stream().map(up -> new ReplyKeyboardTools.ButtonWithPath(
                        up.isOwn() ?
                                MessageFormat.format(
                                        properties.getProperty("botstates.manage.manage.ownProject"),
                                        up.getProject().getName()) : up.getProject().getName(),
                        "selectActionForProject",
                        up.getProject().getId().toString())).collect(Collectors.toList()), 3
        );

        keyboardMarkup.getKeyboard().add(
                ReplyKeyboardTools.createListInlineKeyboardButtons(
                        ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
                                properties.getProperty("botstates.manage.manage.addProject"),
                                "addProject", null),
                        ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
                                properties.getProperty("botstates.exit"),
                                "exit", null)));

        sendMessage.setReplyMarkup(keyboardMarkup);

        execute.accept(sendMessage);
        return new ActionState("selectActionForProject");
    }

    @BotAction(path = "exit", callback = true)
    public ActionState exit(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {

        Message message = botUpdate.getUpdate().getCallbackQuery().getMessage();
        execute.accept(
                MessageTools.deleteMessage(message.getChatId().toString(),
                        message.getMessageId()));
        return null;
    }

    @BotAction(path = "selectActionForProject")
    public ActionState selectActionForProject(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {
        Update update = botUpdate.getUpdate();
        User user = update.hasCallbackQuery() ? update.getCallbackQuery().getFrom() : update.getMessage().getFrom();

        return selectActionManage(execute, botUpdate.getActionData(), user);
    }

    @BotAction(path = "selectActionForProject", callback = true)
    public ActionState selectActionForProjectCallback(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {

        Update update = botUpdate.getUpdate();
        Message message = update.getCallbackQuery().getMessage();
        User user = update.getCallbackQuery().getFrom();

        execute.accept(MessageTools.deleteMessage(user.getId().toString(), message.getMessageId()));

        return selectActionManage(execute, update.getCallbackQuery().getData(), user);
    }

    private ActionState selectActionManage(Consumer<BotApiMethod> execute, String projectId, User user) {

        UserProject up = projectService.getProjectInfoById(user.getId(), projectId);

        SendMessage sendMessage = MessageTools.createSendMessage(user.getId().toString());
        sendMessage.setText(MessageFormat.format(
                properties.getProperty("botstates.manage.manage.selectAction"),
                up.getProject().getName()));

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttonsLineProjectAction = new ArrayList<>();
        if (up.isActive()) {
            buttonsLineProjectAction.add(ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
                    properties.getProperty("botstates.manage.manage.archive"),
                    "archive", projectId));
        } else {
            buttonsLineProjectAction.add(ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
                    properties.getProperty("botstates.manage.manage.unarchive"),
                    "unarchive", projectId));
        }
        buttonsLineProjectAction.add(ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
                properties.getProperty("botstates.manage.manage.leave"),
                "leave", projectId));
        buttonsLineProjectAction.add(ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
                properties.getProperty("botstates.manage.manage.rename"),
                "renameProject", projectId));

        buttons.add(buttonsLineProjectAction);
        buttons.add(ReplyKeyboardTools.createListInlineKeyboardButtons(
                ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
                        properties.getProperty("botstates.manage.manage.users"),
                        "users", projectId)));
        buttons.add(ReplyKeyboardTools.createListInlineKeyboardButtons(
                ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
                        properties.getProperty("botstates.back"),
                        "manage_project", null),
                ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
                        properties.getProperty("botstates.exit"),
                        "exit", null)));

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        execute.accept(sendMessage);
        return null;
    }

}
