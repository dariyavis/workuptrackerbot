package com.workuptrackerbot.main;

import com.workuptrackerbot.bottools.springbottools.Bot;
import com.workuptrackerbot.bottools.springbottools.SpringBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.util.Properties;

@Component
//@Bot(username="WorkUpTimeBot", token="1379456077:AAE3ncbYpMKqogz1jqVz08enTS_5epkHJfs")
@Bot
public class WorkUpTrackerBot extends SpringBot {

    @Autowired
    private Properties properties;

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @Override
    public String getBotUsername() {
        return properties.getProperty("bot.username");
    }

    @Override
    public String getBotToken() {
        return properties.getProperty("bot.token");
    }
}
