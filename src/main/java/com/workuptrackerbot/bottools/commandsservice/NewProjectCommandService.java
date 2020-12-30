package com.workuptrackerbot.bottools.commandsservice;

import com.workuptrackerbot.entity.Project;
import com.workuptrackerbot.entity.UserProject;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class NewProjectCommandService {

    private static int COUNT_BUTTONS_IN_LINE = 3;

    public static ReplyKeyboard createInlineKeyboardProject(List<Project> projects) {

        if(projects.isEmpty()) {
            ReplyKeyboardRemove keyboardRemove = new ReplyKeyboardRemove();
            keyboardRemove.setSelective(false);
            return keyboardRemove;
        }

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);

        List<KeyboardRow> keyboardRow = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        for (int i = 0; i < projects.size(); i++) {
            if(i%COUNT_BUTTONS_IN_LINE == 0){
                row = new KeyboardRow();
                keyboardRow.add(row);
            }
            row.add(projects.get(i).getName());
        }

        keyboardMarkup.setKeyboard(keyboardRow);
//        keyboardMarkup.setOneTimeKeyboard(true);
        return keyboardMarkup;
    }

}
