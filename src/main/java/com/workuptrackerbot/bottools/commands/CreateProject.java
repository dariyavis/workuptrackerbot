package com.workuptrackerbot.bottools.commands;

import com.workuptrackerbot.bottools.springbottools.annotations.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.User;

import java.util.Properties;

@com.workuptrackerbot.bottools.springbottools.annotations.BotCommand(command="/new_project")
public class CreateProject extends Command {

    @Autowired
    private Properties properties;

/*    @Override
    public CommandState handler(User user, Chat chat) {
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId());
        message.setText(properties.getProperty("command.createcommand.enterNameProject"));
        CreateProject createProject = this;
        //BiFunction<User, Chat, BotApiMethod> biFunction = (user1, chat1) -> createProject.addProject(user1, chat1);

        CommandState commandState = new CommandState();
        commandState.setBotApiMethod(message);
        commandState.setChat(chat);
        commandState.setUser(user);
        commandState.setNext("add");

        return commandState;
    }
 */

    @Answer(index = 0)
    public BotApiMethod nameQuestion(Message message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChat().getId());
        sendMessage.setText(properties.getProperty("command.createcommand.enterNameProject"));
        return sendMessage;
    }

    @Answer(index = 1)
    public BotApiMethod addProject(Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChat().getId());
        sendMessage.setText(properties.getProperty("command.createcommand.addedProject") + " " + message.getText());
        return sendMessage;
    }

    public void test() {
    }
}
