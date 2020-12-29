package com.workuptrackerbot.main;


import com.workuptrackerbot.bottools.springbottools.commands.commandsdepricated.Keyboard;
import com.workuptrackerbot.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.api.objects.replykeyboard.*;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Service
public class BotService {

    @Autowired
    private Properties properties;

    @Autowired
    private TrackerService trackerService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    public BotService() {

    }

    public SendMessage onUpdateReceivedMsg(Update update) {

        SendMessage message = new SendMessage();
        message.setChatId(update.getMessage().getChatId());
        message.setText(properties.getProperty("message.text.welcome"));
        message.enableMarkdown(true);
        ReplyKeyboardRemove keyboardRemove = new ReplyKeyboardRemove();
        keyboardRemove.setSelective(false);
        message.setReplyMarkup(keyboardRemove);
        return message;
    }


//    private ReplyKeyboard setInline() {
//                ForceReplyKeyboard - обязательный ответ на сообщение
//        //        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
//        //        List<InlineKeyboardButton> buttons1 = new ArrayList<>();
//        //        buttons1.add(new InlineKeyboardButton().setText("Start uuu").setCallbackData("1"));
//        //        buttons1.add(new InlineKeyboardButton().setText("Start ddd").setCallbackData("2"));
//        //        buttons1.add(new InlineKeyboardButton().setText("Start ddd").setCallbackData("3"));
//        //        buttons.add(buttons1);
//
//        //        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();
//        //        markupKeyboard.setKeyboard(buttons);
//        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
//        keyboardMarkup.setResizeKeyboard(true);
//        // Create the keyboard (list of keyboard rows)
//        List<KeyboardRow> keyboard = new ArrayList<>();
//        // Create a keyboard row
//        KeyboardRow row = new KeyboardRow();
//        // Set each button, you can also use KeyboardButton objects if you need something else than text
//        row.add("Button 1");
//        row.add("Button 2");
//        row.add("Button 3");
//        // Add the first row to the keyboard
//        keyboard.add(row);
//        // Create another keyboard row
//        //        row = new KeyboardRow();
//        //        // Set each button for the second line
//        //        row.add("Row 2 Button 1");
//        //        row.add("Row 2 Button 2");
//        //        row.add("Row 2 Button 3");
//        //        // Add the second row to the keyboard
//        //        keyboard.add(row);
//        // Set the keyboard to the markup
//        keyboardMarkup.setKeyboard(keyboard);
//        keyboardMarkup.setOneTimeKeyboard(true);
//        // Add it to the message
//
//
//        return keyboardMarkup;
//    }


    public SendMessage commandStart(User user, Chat chat) {

        logger.info("User {} start work with bot", user.getUserName());

//        userService.createUser(user);

        SendMessage message = new SendMessage();
        message.setChatId(chat.getId());
        message.setText(properties.getProperty("message.text.welcome"));
        message.enableMarkdown(true);
        message.setReplyMarkup(createInlineKeyboard(Keyboard.START));
        return message;
    }

    private ReplyKeyboard createInlineKeyboard(Keyboard... keyboards) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttons1 = new ArrayList<>();
        for (int i = 0; i < keyboards.length; i++) {
            buttons1.add(new InlineKeyboardButton()
                    .setText(properties.getProperty(keyboards[i].getButton()))
                    .setCallbackData(properties.getProperty(keyboards[i].getCallbackdata())));
        }
        buttons.add(buttons1);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }

    public SendMessage commandStartTracking(Update update) {

        Date date = new Date(update.getCallbackQuery().getMessage().getDate()*1000L);

//        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//        dateFormat.format(date);

        User user = update.getCallbackQuery().getFrom();

//        Logger logger = Logger.getLogger(BotService.class);
//        logger.info("User " + user.getUserName() + " start logging time at " + dateFormat.format(date));


        trackerService.startLog(user.getUserName(), date);
        SendMessage message = new SendMessage();
        message.setChatId(update.getCallbackQuery().getMessage().getChatId());
        message.setText(properties.getProperty(Keyboard.START.getMessage()));
        message.enableMarkdown(true);
        message.setReplyMarkup(createInlineKeyboard(Keyboard.STOP));
        return message;
    }

    public SendMessage commandStopTracking(Update update) {
        Date date = new Date(update.getCallbackQuery().getMessage().getDate()*1000L);

        //        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        //        dateFormat.format(date);

        User user = update.getCallbackQuery().getFrom();

        //        Logger logger = Logger.getLogger(BotService.class);
        //        logger.info("User " + user.getUserName() + " start logging time at " + dateFormat.format(date));


        String result = trackerService.stopLog(user.getUserName(), date);
        SendMessage message = new SendMessage();
        message.setChatId(update.getCallbackQuery().getMessage().getChatId());
        message.setText(properties.getProperty(Keyboard.STOP.getMessage()) + result);
        message.enableMarkdown(true);
        message.setReplyMarkup(createInlineKeyboard(Keyboard.START));
        return message;
    }
}
