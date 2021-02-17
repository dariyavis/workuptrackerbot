package com.workuptrackerbot.bottools.springbottools.commands;



import com.workuptrackerbot.bottools.springbottools.annotations.LastIndexCommand;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class Command {

    private Map<Integer, Function<Message, BotApiMethod>> answers = new HashMap<>();

    @LastIndexCommand
    protected Integer last_index = 0;

    public CommandState handler(CommandState commandState, Message message) {

        Integer index = commandState.getIndex() + 1;

        commandState.setBotApiMethod(answers.get(index).apply(message));
        commandState.setIndex(last_index.equals(index)?null:index);
        return  commandState;
    }

    public Map<Integer, Function<Message, BotApiMethod>> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Integer, Function<Message, BotApiMethod>> answers) {
        this.answers = answers;
    }
}
