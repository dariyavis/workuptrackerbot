package com.workuptrackerbot.bottools.springbottools;

import com.workuptrackerbot.bottools.springbottools.callbackquery.BotStateInteroperable;
import com.workuptrackerbot.bottools.springbottools.callbackquery.CallbackQueryProxy;
import com.workuptrackerbot.bottools.springbottools.commands.ActionState;
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

    private final Map<String, BiFunction<Consumer<BotApiMethod>, Update, String>> states = new HashMap<>();
    private final Map<String, BiFunction<Consumer<BotApiMethod>, Update, String>> commands = new HashMap<>();
    private final Map<String, BiFunction<Consumer<BotApiMethod>, Update, String>> callbackQueries = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasCallbackQuery()) {
//            deleteMessage(update.getCallbackQuery().getMessage().getChatId().toString(), update.getCallbackQuery().getMessage().getMessageId());
            CallbackQueryProxy queryProxy = new CallbackQueryProxy(update.getCallbackQuery());
            update.setCallbackQuery(queryProxy);

            ;
            updateCommandState(
                    update.getCallbackQuery().getFrom(),
                    callbackQueries.get(queryProxy.getPath()).apply(this::botExecuter, update));
            return;
        }
        Message message = update.getMessage();
        if (message.isCommand()) {
            updateCommandState(
                    update.getMessage().getFrom(),
                    commands.get(message.getText()).apply(this::botExecuter, update));
            return;
        }

        ActionState actionState = getActionState(update.getMessage().getFrom());
        if (actionState != null && actionState.getAction() != null) {
            updateCommandState(
                    update.getMessage().getFrom(),
                    states.get(actionState.getAction()).apply(this::botExecuter, update));
            return;
        }

        onUpdateReceivedMessage(update);
    }

    private void updateCommandState(User user, String path) {
        saveActionState(new ActionState(user, path));
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
    public void addBotState(String path, BiFunction<Consumer<BotApiMethod>, Update, String> handler) {
        states.put(path, handler);
    }

    @Override
    public void addCommand(String path, BiFunction<Consumer<BotApiMethod>, Update, String> handler) {
        commands.put("/" + path, handler);
    }

    @Override
    public void addCallback(String path, BiFunction<Consumer<BotApiMethod>, Update, String> handler) {
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
