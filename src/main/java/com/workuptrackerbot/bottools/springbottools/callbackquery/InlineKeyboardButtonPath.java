package com.workuptrackerbot.bottools.springbottools.callbackquery;

import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class InlineKeyboardButtonPath extends InlineKeyboardButton {

    public final static String FIELD_DATA = "data";
    public final static String FIELD_PATH = "path";

    public void setCallbackData(String callbackData) {
        super.setCallbackData(puttingData(FIELD_DATA, callbackData));
    }

    public void setPath(String path) {
        super.setCallbackData(puttingData(FIELD_PATH, path));
    }

    private String puttingData(String field, String data){
        String callbackDataSuper = super.getCallbackData();
        if(callbackDataSuper!=null){
            return new JSONObject(super.getCallbackData()).put(field, data).toString();
        }
        else {
            return new JSONObject().put(field, data).toString();
        }
    }

    private String getPuttingData(String field){
        String callbackDataSuper = super.getCallbackData();
        if(callbackDataSuper!=null){
            return new JSONObject(super.getCallbackData()).get(field).toString();
        }
        else {
            return null;
        }
    }
}
