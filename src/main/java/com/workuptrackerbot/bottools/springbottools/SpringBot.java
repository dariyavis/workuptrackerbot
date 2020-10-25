package com.workuptrackerbot.bottools.springbottools;

import com.workuptrackerbot.bottools.commands.BotCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class SpringBot extends TelegramLongPollingBot implements CommandInterceptorable {

    private TelegramBotsApi telegramBotsApi;

    private Map<String, BotCommandHandler> commands = new HashMap<>();

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
        if (message != null && message.isCommand()) {
            try {
                this.execute(commands.get(message.getText()).handler(message.getFrom(), message.getChat()));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            return;
        }
    }

//    @Override
//    public void onUpdatesReceived(List<Update> updates) {
//        //todo
//        System.out.println(updates.size());
//    }

    @Override
    public void addCommand(String code, BotCommandHandler command) {
        commands.put(code, command);
    }

    @Override
    public void onClosing() {

    }
}
