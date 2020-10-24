package com.workuptrackerbot.bottools.commands;

import com.workuptrackerbot.bottools.springbottools.BotCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;

import java.lang.invoke.MethodHandles;

@BotCommand(command="/start")
@Component
public class StartCommand implements BotCommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public void handler(User user, Chat chat) {
        logger.info("User {} start work with bot", user.getUserName());
    }


    //    @Override
//    public void execute(User user, Chat chat) {
//        logger.info("User {} start work with bot", user.getUserName());
//    }
}
