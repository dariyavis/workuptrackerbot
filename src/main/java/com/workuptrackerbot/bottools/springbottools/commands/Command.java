package com.workuptrackerbot.bottools.springbottools.commands;



import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class Command {

    private Map<Integer, Function<Message, BotApiMethod>> answers = new HashMap<>();

    public CommandState handler(CommandState commandState, Message message) {

        Integer index = commandState.getIndex() + 1;

//        CommandState commandState = new CommandState();
        commandState.setBotApiMethod(answers.get(index).apply(message));
        commandState.setIndex(index);
        return commandState;
    }

    public Map<Integer, Function<Message, BotApiMethod>> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Integer, Function<Message, BotApiMethod>> answers) {
        this.answers = answers;
    }
}
