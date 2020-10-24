package com.workuptrackerbot.main;

import com.workuptrackerbot.bottools.springbottools.Bot;
import com.workuptrackerbot.bottools.springbottools.SpringBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;

@Component
//@Bot(username="WorkUpTimeBot", token="1379456077:AAE3ncbYpMKqogz1jqVz08enTS_5epkHJfs")
@Bot
public class WorkUpTrackerBot extends SpringBot {

    @Autowired
    private TelegramBotsApi telegramBotsApi;

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @PostConstruct
    public void postConstruct() {
//        try {
//            telegramBotsApi.registerBot(this);
            logger.info("Bot {} registered", getBotUsername());
//        } catch (TelegramApiRequestException e) {
//            e.printStackTrace();
//        }
    }

}
