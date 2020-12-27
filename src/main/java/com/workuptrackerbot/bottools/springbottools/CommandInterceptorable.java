package com.workuptrackerbot.bottools.springbottools;

import com.workuptrackerbot.bottools.commands.Command;

public interface CommandInterceptorable {

    void addCommand(String code, Command command);
}
