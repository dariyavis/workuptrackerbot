package com.workuptrackerbot.bottools.tlgmtools;

import com.workuptrackerbot.entity.Interval;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class ReplyKeyboardTools {

    private static int COUNT_BUTTONS_IN_LINE = 3;

    public static ReplyKeyboard createReplyKeyboardMarkup(List<Object> objects, Function<Object, String> extractText) {

        if (objects.isEmpty()) {

            ReplyKeyboardRemove keyboardRemove = new ReplyKeyboardRemove();
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

    public static ReplyKeyboard createInlineKeyboard(List<Object> objects, Function<Object, String> extractText, Function<Object, String> extractId) {

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttonsLine = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            if (i % COUNT_BUTTONS_IN_LINE == 0) {
                buttonsLine = new ArrayList<>();
                buttons.add(buttonsLine);
            }
            buttonsLine.add(new InlineKeyboardButton()
                    .setText(extractText.apply(objects.get(i)))
                    .setCallbackData(extractId.apply(objects.get(i))));
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }

    public static ReplyKeyboard createInlineKeyboard(Object object, Function<Object, String> extractText, Function<Object, String> extractId) {

        List<Object> list = new LinkedList<>();
        list.add(object);
        return createInlineKeyboard(list, extractText, extractId);
    }
}
