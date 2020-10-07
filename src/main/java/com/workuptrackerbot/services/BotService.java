package com.workuptrackerbot.services;

import com.workuptrackerbot.data.Bot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Service
public class BotService {

    @Autowired
    private Properties properties;

    public SendMessage onUpdateReceivedMsg(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText(properties.getProperty("message.text.welcome"));
        message.enableMarkdown(true);
        message.setReplyMarkup(setInline());
        return message;
    }







    private ReplyKeyboard setInline() {
//        ForceReplyKeyboard - обязательный ответ на сообщение
//        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
//        List<InlineKeyboardButton> buttons1 = new ArrayList<>();
//        buttons1.add(new InlineKeyboardButton().setText("Start uuu").setCallbackData("1"));
//        buttons1.add(new InlineKeyboardButton().setText("Start ddd").setCallbackData("2"));
//        buttons1.add(new InlineKeyboardButton().setText("Start ddd").setCallbackData("3"));
//        buttons.add(buttons1);

//        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
//        markupKeyboard.setKeyboard(buttons);
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        // Create the keyboard (list of keyboard rows)
        List<KeyboardRow> keyboard = new ArrayList<>();
        // Create a keyboard row
        KeyboardRow row = new KeyboardRow();
        // Set each button, you can also use KeyboardButton objects if you need something else than text
        row.add("Button 1");
        row.add("Button 2");
        row.add("Button 3");
        // Add the first row to the keyboard
        keyboard.add(row);
        // Create another keyboard row
        //        row = new KeyboardRow();
        //        // Set each button for the second line
        //        row.add("Row 2 Button 1");
        //        row.add("Row 2 Button 2");
        //        row.add("Row 2 Button 3");
        //        // Add the second row to the keyboard
        //        keyboard.add(row);
        // Set the keyboard to the markup
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setOneTimeKeyboard(true);
        // Add it to the message



        return keyboardMarkup;
    }


    public void commandStart(User user, Chat chat) {
        System.out.println("Command Start DOOOOOOO");
    }
}
