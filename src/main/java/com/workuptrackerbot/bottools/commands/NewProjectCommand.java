package com.workuptrackerbot.bottools.commands;

import com.workuptrackerbot.bottools.springbottools.annotations.Answer;
import com.workuptrackerbot.entity.UserProject;
import com.workuptrackerbot.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@com.workuptrackerbot.bottools.springbottools.annotations.BotCommand(command="/new_project")
public class NewProjectCommand extends Command {

    private int COUNT_BUTTONS_IN_LINE = 3;

    @Autowired
    private Properties properties;

    @Autowired
    private ProjectService projectService;

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

        try {
            List<UserProject> ups = projectService.addProject(message.getFrom().getUserName(), message.getText());
            sendMessage.setText(properties.getProperty("command.createcommand.addedProject") + " " + message.getText());
            sendMessage.setReplyMarkup(createInlineKeyboardProject(ups));
        } catch (Exception e) {
            sendMessage.setText(properties.getProperty("command.createcommand.projectexist"));
        }


        return sendMessage;
    }

//    private ReplyKeyboard createInlineKeyboardProject(List<UserProject> ups) {
//
//        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
//        List<InlineKeyboardButton> buttonsLine = new ArrayList<>();
//        for (int i = 0; i < ups.size(); i++) {
//            if(i%COUNT_BUTTONS_IN_LINE == 0){
//                buttonsLine = new ArrayList<>();
//                buttons.add(buttonsLine);
//            }
//            buttonsLine.add(new InlineKeyboardButton()
//                    .setText(ups.get(i).getProject().getName())
//                    .setCallbackData(ups.get(i).getId().toString()));
//        }
//
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        inlineKeyboardMarkup.setKeyboard(buttons);
//        return inlineKeyboardMarkup;
//    }

    private ReplyKeyboard createInlineKeyboardProject(List<UserProject> ups) {

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRow = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        for (int i = 0; i < ups.size(); i++) {
            if(i%COUNT_BUTTONS_IN_LINE == 0){
                row = new KeyboardRow();
                keyboardRow.add(row);
            }
            row.add(ups.get(i).getProject().getName());
        }

        keyboardMarkup.setKeyboard(keyboardRow);
//        keyboardMarkup.setOneTimeKeyboard(true);
        return keyboardMarkup;
    }

}
