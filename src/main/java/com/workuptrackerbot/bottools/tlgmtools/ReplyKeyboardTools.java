package com.workuptrackerbot.bottools.tlgmtools;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class ReplyKeyboardTools {

    private static int COUNT_BUTTONS_IN_LINE = 3;

    public static <T> ReplyKeyboard createReplyKeyboardMarkup(List<T> objects, Function<T, String> extractText) {

        if (objects.isEmpty()) {

            ReplyKeyboardRemove keyboardRemove = new ReplyKeyboardRemove(true);
            keyboardRemove.setSelective(false);
            return keyboardRemove;
        }

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRow = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        for (int i = 0; i < objects.size(); i++) {
            if (i % COUNT_BUTTONS_IN_LINE == 0) {
                row = new KeyboardRow();
                keyboardRow.add(row);
            }
//            row.add(objects.get(i).getName());
            row.add(extractText.apply(objects.get(i)));
        }

        keyboardMarkup.setKeyboard(keyboardRow);
//        keyboardMarkup.setOneTimeKeyboard(true);
        return keyboardMarkup;
    }

    public static <T> ReplyKeyboard createInlineKeyboard(List<T> objects, Function<T, String> extractText, Function<T, String> extractId) {

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttonsLine = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            if (i % COUNT_BUTTONS_IN_LINE == 0) {
                buttonsLine = new ArrayList<>();
                buttons.add(buttonsLine);
            }
            InlineKeyboardButton ikbutton = new InlineKeyboardButton();
            ikbutton.setText(extractText.apply(objects.get(i)));
            ikbutton.setCallbackData(extractId.apply(objects.get(i)));
            buttonsLine.add(ikbutton);
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }

    public static <T> ReplyKeyboard createInlineKeyboard(T object, Function<T, String> extractText, Function<T, String> extractId) {

        List<T> list = new LinkedList<>();
        list.add(object);
        return createInlineKeyboard(list, extractText, extractId);
    }
}
