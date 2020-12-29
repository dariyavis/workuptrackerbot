package com.workuptrackerbot.bottools.springbottools;

import com.workuptrackerbot.bottools.commands.Command;
import com.workuptrackerbot.bottools.commands.CommandState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

public abstract class SpringBot extends TelegramLongPollingBot implements CommandInterceptorable {

    private TelegramBotsApi telegramBotsApi;

    private Map<String, Command> commands = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @PostConstruct
    public void postConstruct() {
        telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(this);
        logger.info("Bot {} registered", getBotUsername());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {

//        if (update.hasCallbackQuery()) {
//            this.execute(keybords.get(update.getCallbackQuery().getData()).apply(update));
//            return;
//        }
        Message message = update.getMessage();
        User user = message.getFrom();
        Chat chat = message.getChat();
        if (message != null && message.isCommand()) {
            String commandKey = message.getText();
                executeCommandState(new CommandState(user, chat, commandKey), message);
            return;
        } else {
            CommandState commandState = getCommandState(user,chat);
            //todo если состояние не пустое, выполнить из команды и передать еще сообщение
            if(commandState != null) {
                executeCommandState(commandState, message);
            } else {

            }
        }
    }

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
