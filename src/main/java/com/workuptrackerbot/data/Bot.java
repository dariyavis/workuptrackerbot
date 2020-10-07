package com.workuptrackerbot.data;

import com.workuptrackerbot.data.commands.WorkupTrackerBotCommand;
import com.workuptrackerbot.services.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.bots.commandbot.commands.CommandRegistry;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.workuptrackerbot.data.commands.Command.START;

@Component
public class Bot extends TelegramLongPollingBot {

    @Autowired
    private TelegramBotsApi telegramBotsApi;
    @Autowired
    private BotService botService;
    @Autowired
    private Properties properties;
//    private Map<> commands;


    @PostConstruct
    public void postConstruct() {

        try {
            telegramBotsApi.registerBot(this);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

        SendMessage message = new SendMessage();
        message.setChatId("134789250");
        message.setText(":: Server started ::");
        try {
            this.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для приема сообщений.
     *
     * @param update
     *         Содержит сообщение от пользователя.
     */
    @Override
    public void onUpdateReceived(Update update) {
        try {
            this.execute(botService.onUpdateReceivedMsg(update));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

//

    /**
     * Метод возвращает имя бота, указанное при регистрации.
     *
     * @return имя бота
     */
    @Override
    public String getBotUsername() {
        return "WorkUpTimeBot";
    }

    /**
     * Метод возвращает token бота для связи с сервером Telegram
     *
     * @return token для бота
     */
    @Override
    public String getBotToken() {
        return "1379456077:AAE3ncbYpMKqogz1jqVz08enTS_5epkHJfs";
    }

    /**
     //     * Метод для настройки сообщения и его отправки.
     //     *
     //     * @param chatId
     //     *         id чата
     //     * @param s
     //     *         Строка, которую необходимот отправить в качестве сообщения.
     //     */
/**  public synchronized void sendMsg(String chatId, String s) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(chatId);
            sendMessage.setText(s);
            try {
                sendMessage(sendMessage);
            } catch (TelegramApiException e) {
                //            Log.log(Level.SEVERE, "Exception: ", e.toString());
            }
        }
 */
    public void sendCustomKeyboard(String chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("Custom message text");

        // Create ReplyKeyboardMarkup object
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
        message.setReplyMarkup(keyboardMarkup);

        try {
            // Send the message
            this.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
