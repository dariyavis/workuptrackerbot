package com.workuptrackerbot.bottools.botstates;

import com.workuptrackerbot.bottools.springbottools.annotations.BotAction;
import com.workuptrackerbot.bottools.springbottools.annotations.HasBotAction;
import com.workuptrackerbot.bottools.springbottools.commands.ActionState;
import com.workuptrackerbot.bottools.springbottools.commands.BotUpdate;
import com.workuptrackerbot.bottools.tlgmtools.MessageTools;
import com.workuptrackerbot.bottools.tlgmtools.ReplyKeyboardTools;
import com.workuptrackerbot.entity.UserEntity;
import com.workuptrackerbot.entity.UserProject;
import com.workuptrackerbot.service.ProjectService;
import com.workuptrackerbot.service.UserService;
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
public class Users {

    @Autowired
    private Properties properties;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @BotAction(path = "users")
    public ActionState users(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {
        Update update = botUpdate.getUpdate();
        return usersHandler(execute,
                update.getMessage().getFrom(),
                botUpdate.getActionData());
    }

    @BotAction(path = "users", callback = true)
    public ActionState usersCallback(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {
        Update update = botUpdate.getUpdate();

        execute.accept(
                MessageTools.deleteMessage(update.getCallbackQuery().getMessage().getChatId().toString(),
                        update.getCallbackQuery().getMessage().getMessageId()));
        return usersHandler(execute,
                update.getCallbackQuery().getFrom(),
                update.getCallbackQuery().getData());

    }

    private ActionState usersHandler(Consumer<BotApiMethod> execute, User user, String project_id) {

        List<String> usernames = userService.getUserNamesByProjectId(project_id);
        UserProject up = projectService.getProjectInfoById(user.getId(),project_id);
        StringBuilder sb = new StringBuilder();
        sb.append(MessageFormat.format(
                properties.getProperty("botstates.users.text.header"),
                up.getProject().getName()));
        usernames.forEach(username -> sb.append(MessageFormat.format(
                properties.getProperty("botstates.users.text.username"),
                username)));

        SendMessage sendMessage = MessageTools.createSendMessage(user.getId().toString());
        sendMessage.setText(sb.toString());

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        if(up.isOwn()) {
            buttons.add(ReplyKeyboardTools.createListInlineKeyboardButtons(
                    ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
                            properties.getProperty("botstates.users.button.addUser"),
                            "addUser", project_id),
                    ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
                            properties.getProperty("botstates.users.button.removeUser"),
                            "removeUser", project_id)));
        }
        buttons.add(ReplyKeyboardTools.createListInlineKeyboardButtons(
                ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
                        properties.getProperty("botstates.back"),
                        "selectActionForProject", project_id),
                ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
                        properties.getProperty("botstates.exit"),
                        "exit", null)));

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        execute.accept(sendMessage);

        return null;

    }

    @BotAction(path = "addUser", callback = true)
    public ActionState addUserToProjectQuestion(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {

        Message message = botUpdate.getUpdate().getCallbackQuery().getMessage();

        execute.accept(
                MessageTools.deleteMessage(message.getChatId().toString(),
                        message.getMessageId()));

        SendMessage sendMessage = MessageTools.createSendMessage(message.getChat().getId().toString());
        sendMessage.setText(properties.getProperty("botstates.users.addUserText.enterUserName"));
        execute.accept(sendMessage);
        return new ActionState("addUserToProject", botUpdate.getUpdate().getCallbackQuery().getData());
    }

    @BotAction(path = "addUserToProject")
    public ActionState addUserToProject(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {

        Update update = botUpdate.getUpdate();
        Message message = update.getMessage();
        String username = message.getEntities().get(0).getText().split("@")[1];

        UserEntity newUser = userService.getOrCreateUserByUserName(username);
        UserProject up = projectService.addUserToProject(newUser, botUpdate.getActionData());

        SendMessage sendMessage = MessageTools.createSendMessage(message.getChat().getId().toString());
        sendMessage.setText(
                MessageFormat.format(
                        properties.getProperty("botstates.users.addedUserText.enterUserName"),
                        newUser.getUsername(), up.getProject().getName()));
        execute.accept(sendMessage);

        if (userService.isRegistedUserByUserName(username)) {
            SendMessage sendMessageToNewUser = MessageTools.createSendMessage(newUser.getTlgId().toString());
            sendMessageToNewUser.setText(
                    MessageFormat.format(
                            properties.getProperty("botstates.users.addedUserText.projectForNewUser"),
                            up.getProject().getName()));
            execute.accept(sendMessageToNewUser);
        }
        return new ActionState("users", botUpdate.getActionData(), true);
    }

    @BotAction(path = "removeUser", callback = true)
    public ActionState removeUserToProjectQuestion(Consumer<BotApiMethod> execute, BotUpdate botUpdate) {

        Update update = botUpdate.getUpdate();

        execute.accept(
                MessageTools.deleteMessage(update.getCallbackQuery().getMessage().getChatId().toString(),
                        update.getCallbackQuery().getMessage().getMessageId()));

        String project_id = update.getCallbackQuery().getData();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChat().getId().toString());
        sendMessage.setText(properties.getProperty("botstates.users.removeUser.selectUserName"));

        List<String> usernames = userService.getUserNamesByProjectId(project_id);

        sendMessage.setReplyMarkup(
                ReplyKeyboardTools.createInlineKeyboardMarkupWithPath(
                        usernames.stream().map(username -> new ReplyKeyboardTools.ButtonWithPath(
                                "@" + username,
                                "removeUserFromProject",
                                project_id + ":" + username))
                                .collect(Collectors.toList()), 3
                ));
        execute.accept(sendMessage);

        //todo сделать удаление не своего юзера, back from удаления юзера,
        // возможно сделать сохранение данных для юзера после удаления (флаг)
        // и невозможность достать его из архива если не владелец

        return new ActionState("user", botUpdate.getActionData(), true);
    }
}
