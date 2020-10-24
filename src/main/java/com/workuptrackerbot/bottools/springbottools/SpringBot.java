package com.workuptrackerbot.bottools.springbottools;

import com.workuptrackerbot.bottools.commands.BotCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SpringBot extends TelegramLongPollingBot implements CommandInterceptorable {

    private String botUsername;
    private String botToken;

    private Map<String, BotCommandHandler> commands = new HashMap<>();

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @Override
    public void onUpdateReceived(Update update) {

//        if (update.hasCallbackQuery()) {
//            this.execute(keybords.get(update.getCallbackQuery().getData()).apply(update));
//            return;
//        }

        Message message = update.getMessage();
        if (message != null && message.isCommand()) {
//            this.execute(commands.get(message.getText()).apply(message.getFrom(), message.getChat()));
            commands.get(message.getText()).handler(message.getFrom(), message.getChat());
            return;
        }
//        sendMesssage();
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        //todo
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void addCommand(String code, BotCommandHandler command) {
        commands.put(code, command);
    }

    @Override
    public void onClosing() {

    }
}
