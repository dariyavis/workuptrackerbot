package com.workuptrackerbot.bottools.commands;

import com.workuptrackerbot.bottools.springbottools.Answer;
import com.workuptrackerbot.bottools.springbottools.BotCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;

import java.util.Properties;
import java.util.function.BiFunction;

@BotCommand(command="/new_project")
public class CreateProject implements BotCommandHandler {

    @Autowired
    private Properties properties;

    @Override
    public BotApiMethod handler(User user, Chat chat) {
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId());
        message.setText(properties.getProperty("command.createcommand.text1"));
        CreateProject createProject = this;
        BiFunction<User, Chat, BotApiMethod> biFunction = (user1, chat1) -> createProject.addProject(user1, chat1);

        return message;
    }

    @Answer(index=0)
    private BotApiMethod addProject(User user, Chat chat){
        return null;
    }
}
