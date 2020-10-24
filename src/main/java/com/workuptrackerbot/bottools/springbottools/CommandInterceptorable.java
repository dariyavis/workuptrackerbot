package com.workuptrackerbot.bottools.springbottools;

import com.workuptrackerbot.bottools.commands.BotCommandHandler;

public interface CommandInterceptorable {

    void addCommand(String code, BotCommandHandler command);
}
