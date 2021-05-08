package com.workuptrackerbot.bottools.springbottools;

import com.workuptrackerbot.bottools.springbottools.callbackquery.BotStateInteroperable;
import com.workuptrackerbot.bottools.springbottools.callbackquery.CallbackQueryProxy;
import com.workuptrackerbot.bottools.springbottools.commands.ActionState;
import com.workuptrackerbot.bottools.springbottools.commands.BotUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public abstract class SpringBot extends TelegramLongPollingBot implements BotStateInteroperable {

    private final Map<String, BiFunction<Consumer<BotApiMethod>, BotUpdate, ActionState>> states = new HashMap<>();
    private final Map<String, BiFunction<Consumer<BotApiMethod>, BotUpdate, ActionState>> commands = new HashMap<>();
    private final Map<String, BiFunction<Consumer<BotApiMethod>, BotUpdate, ActionState>> callbackQueries = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasCallbackQuery()) {
            CallbackQueryProxy queryProxy = new CallbackQueryProxy(update.getCallbackQuery());
            update.setCallbackQuery(queryProxy);

            ;
            updateCommandState(
                    update,
                    doState(callbackQueries, queryProxy.getPath(), update, null));
            return;
        }
        Message message = update.getMessage();
        if (message.isCommand()) {
            updateCommandState(
                    update,
                    doState(commands, message.getText(), update, null));
            return;
        }

        ActionState actionState = getActionState(update.getMessage().getFrom());
        if(actionState.getAction() != null) {
            doActionState(update, actionState);
            return;
        }

        onUpdateReceivedMessage(update);
    }

    private void doActionState(Update update, ActionState actionState){
        if (actionState != null && actionState.getAction() != null) {
            updateCommandState(
                    update,
                    doState(states, actionState.getAction(), update, actionState.getData()));
            return;
        }
    }

    private void updateCommandState(Update update, ActionState actionState) {
        User user = update.hasCallbackQuery()? update.getCallbackQuery().getFrom() : update.getMessage().getFrom();
        if(actionState == null) {
            actionState = new ActionState(user);
        }
        if(actionState.isDoForce()) {
            doActionState(update, actionState);
        } else {
            actionState.setUser(user);
            saveActionState(actionState);
        }
    }

    private ActionState doState(Map<String, BiFunction<Consumer<BotApiMethod>, BotUpdate, ActionState>> statesMap,
                                String action, Update update, String actionData){
        if(statesMap.containsKey(action))
            return statesMap.get(action).apply(this::botExecuter, new BotUpdate(update, actionData));
        return null;
    }

    public void botExecuter(BotApiMethod botApiMethod) {
        try {
            this.execute(botApiMethod);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    protected abstract void onUpdateReceivedCallbackQuery(Update update);

    protected abstract void onUpdateReceivedMessage(Update update);

    @Override
    public void addBotState(String path, BiFunction<Consumer<BotApiMethod>, BotUpdate, ActionState> handler) {
        states.put(path, handler);
    }

    @Override
    public void addCommand(String path, BiFunction<Consumer<BotApiMethod>, BotUpdate, ActionState> handler) {
        commands.put("/" + path, handler);
    }

    @Override
    public void addCallback(String path, BiFunction<Consumer<BotApiMethod>, BotUpdate, ActionState> handler) {
        callbackQueries.put(path, handler);
    }

    @Override
    public void onClosing() {
    }

    public abstract void saveActionState(ActionState actionState);

    public abstract ActionState getActionState(User user);

    protected void deleteMessage(String chatId, Integer messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);
        try {
            this.execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


}
