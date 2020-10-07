package com.workuptrackerbot.services;

import com.workuptrackerbot.data.Bot;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;

@Service
public class BotService {

//    @PostConstruct
//    private void registerBot(){
//        ApiContextInitializer.init();
//        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
//        try {
//            telegramBotsApi.registerBot(new Bot());
//        } catch (TelegramApiRequestException e) {
//            e.printStackTrace();
//        }
//    }
}
