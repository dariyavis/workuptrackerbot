package com.workuptrackerbot.bottools.tlgmtools;

import com.workuptrackerbot.bottools.springbottools.callbackquery.InlineKeyboardButtonPath;
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

    private static int COUNT_BUTTONS_IN_LINE = 4;
    private static int COUNT_BUTTONS_IN_LINE_DEFUULT = 1;

    public static class ButtonWithPath {
        String text;
        String path;
        String data;

        public ButtonWithPath() {
        }

        public ButtonWithPath(String text, String path, String data) {
            this.text = text;
            this.path = path;
            this.data = data;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

    public static <T> ReplyKeyboard createReplyKeyboardMarkup(List<T> objects, Function<T, String> extractText) {

        if (objects.isEmpty()) {
            return ReplyKeyboardTools.removeKeyBord();
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

    public static <T> ReplyKeyboard createInlineKeyboard(List<T> objects, Function<T, String> extractText, Function<T, String> getData) {

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttonsLine = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            if (i % COUNT_BUTTONS_IN_LINE == 0) {
                buttonsLine = new ArrayList<>();
                buttons.add(buttonsLine);
            }
            InlineKeyboardButton ikbutton = new InlineKeyboardButton();
            ikbutton.setText(extractText.apply(objects.get(i)));
            ikbutton.setCallbackData(getData.apply(objects.get(i)));
            buttonsLine.add(ikbutton);
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(buttons);
        return inlineKeyboardMarkup;
    }

    public static <T> ReplyKeyboard createInlineKeyboard(T object, Function<T, String> extractText, Function<T, String> getData) {

        List<T> list = new LinkedList<>();
        list.add(object);
        return createInlineKeyboard(list, extractText, getData);
    }

    public static ReplyKeyboard removeKeyBord() {
        ReplyKeyboardRemove keyboardRemove = new ReplyKeyboardRemove(true);
        keyboardRemove.setSelective(false);
        return keyboardRemove;
    }

    public static InlineKeyboardButton createInlineKeyboardButtonWithPath(String textButton, String path, String data) {
        InlineKeyboardButtonPath ikbutton = new InlineKeyboardButtonPath();
        ikbutton.setText(textButton);
        ikbutton.setPath(path);
        ikbutton.setCallbackData(data);
        return ikbutton;
    }

    public static InlineKeyboardMarkup createInlineKeyboardMarkupWithPath(List<ButtonWithPath> buttons, int countInLines) {
        List<List<InlineKeyboardButton>> listButtons = new ArrayList<>();
        List<InlineKeyboardButton> buttonsLine = new ArrayList<>();
        for (int i = 0; i < buttons.size(); i++) {
            if (i % countInLines == 0) {
                buttonsLine = new ArrayList<>();
                listButtons.add(buttonsLine);
            }
            ButtonWithPath buttonWithPath = buttons.get(i);
            buttonsLine.add(ReplyKeyboardTools.createInlineKeyboardButtonWithPath(
                    buttonWithPath.getText(),
                    buttonWithPath.getPath(),
                    buttonWithPath.getData()));
        }
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(listButtons);
        return inlineKeyboardMarkup;
    }

    public static InlineKeyboardMarkup createInlineKeyboardMarkupWithPath(List<ButtonWithPath> buttons) {
        return createInlineKeyboardMarkupWithPath(buttons, COUNT_BUTTONS_IN_LINE_DEFUULT);
    }

    public static InlineKeyboardMarkup createInlineKeyboardMarkupWithPath(ButtonWithPath button) {
        List<ButtonWithPath> buttons = new LinkedList<>();
        buttons.add(button);
        return createInlineKeyboardMarkupWithPath(buttons, COUNT_BUTTONS_IN_LINE_DEFUULT);
    }
}
