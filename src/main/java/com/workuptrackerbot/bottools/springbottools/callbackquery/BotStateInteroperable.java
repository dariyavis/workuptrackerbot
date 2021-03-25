package com.workuptrackerbot.bottools.springbottools.callbackquery;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public interface BotStateInteroperable {

    void addBotState(String path, BiFunction<Consumer<BotApiMethod>, Update, String> handler);

    void addCommand(String path, BiFunction<Consumer<BotApiMethod>, Update, String> handler);

    void addCallback(String path, BiFunction<Consumer<BotApiMethod>, Update, String> handler);

}
