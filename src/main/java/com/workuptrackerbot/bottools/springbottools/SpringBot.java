package com.workuptrackerbot.bottools.springbottools;

import com.workuptrackerbot.bottools.springbottools.callbackquery.CallbackQueryInterceptorable;
import com.workuptrackerbot.bottools.springbottools.callbackquery.CallbackQueryProxy;
import com.workuptrackerbot.bottools.springbottools.commands.Command;
import com.workuptrackerbot.bottools.springbottools.commands.CommandInterceptorable;
import com.workuptrackerbot.bottools.springbottools.commands.CommandState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class SpringBot extends TelegramLongPollingBot implements CommandInterceptorable, CallbackQueryInterceptorable {

    private Map<String, Command> commands = new HashMap<>();
    private Map<String, Function< Update, BotApiMethod>> callbackQuerys = new HashMap<>();


    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasCallbackQuery()) {
//            deleteMessage(update.getCallbackQuery().getMessage().getChatId().toString(), update.getCallbackQuery().getMessage().getMessageId());
            CallbackQueryProxy queryProxy = new CallbackQueryProxy(update.getCallbackQuery());
            update.setCallbackQuery(queryProxy);

            try {
                this.execute(callbackQuerys.get(queryProxy.getPath()).apply(update));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
//            onUpdateReceivedCallbackQuery(update);
            return;
        }
        Message message = update.getMessage();
        User user = message.getFrom();
        Chat chat = message.getChat();
//        if (message != null && message.isCommand()) {
        if (message.isCommand()) {
            String commandKey = message.getText();
                executeCommandState(new CommandState(user, commandKey), message);
        } else {
            CommandState commandState = getCommandState(user);
            //если состояние не пустое, выполнить из команды и передать еще сообщение
            if(commandState.getIndex() != null) {
                executeCommandState(commandState, message);
            } else {
                //сообщение с названием проекта
                onUpdateReceivedMessage(update);

            }
        }
    }

    protected abstract void onUpdateReceivedCallbackQuery(Update update);

    protected abstract void onUpdateReceivedMessage(Update update);

    private void executeCommandState(CommandState commandState, Message message){
        CommandState commandStateNew =  commands.get(commandState.getCommand()).handler(commandState, message);
        try {
            this.execute(commandState.getBotApiMethod());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        saveCommandState(commandStateNew);
    }

//    @Override
//    public void onUpdatesReceived(List<Update> updates) {
//        //todo
//        System.out.println(updates.size());
//    }

    @Override
    public void addCommand(String code, Command command) {
        commands.put(code, command);
    }

    @Override
    public void addCallBackQuery(String path, Function< Update, BotApiMethod> query){
        callbackQuerys.put(path, query);
    }

    @Override
    public void onClosing() { }

    public abstract void saveCommandState(CommandState commandState);

    public abstract CommandState getCommandState(User user);

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
