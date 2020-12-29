package com.workuptrackerbot.main;

import com.workuptrackerbot.bottools.springbottools.commands.CommandState;
import com.workuptrackerbot.bottools.springbottools.annotations.Bot;
import com.workuptrackerbot.bottools.springbottools.SpringBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;

import java.lang.invoke.MethodHandles;
import java.util.Properties;

@Bot
public class WorkUpTrackerBot extends SpringBot {

    @Autowired
    private Properties properties;

    private CommandState commandStates = null;

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @Override
    public String getBotUsername() {
        return properties.getProperty("bot.username");
    }

    @Override
    public String getBotToken() {
        return properties.getProperty("bot.token");
    }

    @Override
    public void saveCommandState(CommandState commandState) {
        this.commandStates = commandState;
    }

    @Override
    public CommandState getCommandState(User user, Chat chat) {
        //todo тянет из базы если есть
        return commandStates;
    }
}
