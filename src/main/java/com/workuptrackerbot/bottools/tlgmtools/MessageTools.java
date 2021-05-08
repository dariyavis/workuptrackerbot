package com.workuptrackerbot.bottools.tlgmtools;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class MessageTools {

    public static BotApiMethod deleteMessage(String chatId, Integer messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);
        return deleteMessage;
    }

    public static SendMessage createSendMessage(String chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.enableHtml(true);
        return sendMessage;
    }

    public static SendMessage createSendMessage(String chatId, String text) {
        SendMessage sendMessage = MessageTools.createSendMessage(chatId);
        sendMessage.setText(text);
        return sendMessage;
    }
}
