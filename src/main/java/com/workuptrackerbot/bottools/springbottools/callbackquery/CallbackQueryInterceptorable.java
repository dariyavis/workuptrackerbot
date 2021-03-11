package com.workuptrackerbot.bottools.springbottools.callbackquery;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Function;

public interface CallbackQueryInterceptorable {

    void addCallBackQuery(String path, Function<Update, BotApiMethod> query);

}
