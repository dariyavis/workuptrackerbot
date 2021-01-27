package com.workuptrackerbot.bottools.springbottools;

import com.workuptrackerbot.bottools.springbottools.commands.Command;
import com.workuptrackerbot.bottools.springbottools.commands.CommandState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

public abstract class SpringBot extends TelegramLongPollingBot implements CommandInterceptorable {

    private TelegramBotsApi telegramBotsApi;

    private Map<String, Command> commands = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasCallbackQuery()) {
            onUpdateReceivedCallbackQuery(update);
            return;
        }
        Message message = update.getMessage();
        User user = message.getFrom();
        Chat chat = message.getChat();
//        if (message != null && message.isCommand()) {
        if (message.isCommand()) {
            String commandKey = message.getText();
                executeCommandState(new CommandState(user, chat, commandKey), message);
        } else {
            CommandState commandState = getCommandState(user,chat);
            //если состояние не пустое, выполнить из команды и передать еще сообщение
            if(commandState != null) {
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
    public void onClosing() { }

    public abstract void saveCommandState(CommandState commandState);

    public abstract CommandState getCommandState(User user, Chat chat);
}
