package com.workuptrackerbot.bottools.springbottools.commands;

import com.workuptrackerbot.bottools.springbottools.commands.Command;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.function.Function;

public interface CommandInterceptorable {

    void addCommand(String code, Command command);
}
