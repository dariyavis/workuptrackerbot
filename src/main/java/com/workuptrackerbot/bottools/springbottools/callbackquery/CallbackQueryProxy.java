package com.workuptrackerbot.bottools.springbottools.callbackquery;

import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

public class CallbackQueryProxy  extends CallbackQuery {

    public CallbackQueryProxy() {
    }

    public CallbackQueryProxy(String id, User from, Message message, String inlineMessageId, String data, String gameShortName, String chatInstance) {
        super(id, from, message, inlineMessageId, data, gameShortName, chatInstance);
    }

    public CallbackQueryProxy(CallbackQuery callbackQuery) {
        super(callbackQuery.getId(), callbackQuery.getFrom(), callbackQuery.getMessage(),
                callbackQuery.getInlineMessageId(), callbackQuery.getData(), callbackQuery.getGameShortName(), callbackQuery.getChatInstance());
    }

    public String getPath() {
        return getPuttingData(super.getData(), InlineKeyboardButtonPath.FIELD_PATH);
    }

    @Override
    public String getData(){
        return getPuttingData(super.getData(), InlineKeyboardButtonPath.FIELD_DATA);
    }

    private String getPuttingData(String callbackData, String field){
        if(callbackData!=null){
            return new JSONObject(callbackData).get(field).toString();
        }
        else {
            return null;
        }
    }
}
