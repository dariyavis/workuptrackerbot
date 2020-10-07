package com.workuptrackerbot.data.commands;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commandbot.commands.BotCommand;

import java.util.function.BiConsumer;

public class WorkupTrackerBotCommand extends BotCommand {

    private BiConsumer<User, Chat> callback;

    public WorkupTrackerBotCommand(String commandIdentifier, String description, BiConsumer<User, Chat> callback) {
        super(commandIdentifier, description);
    }
    /**
     * реализованный метод класса BotCommand, в котором обрабатывается команда, введенная пользователем
     * @param absSender - отправляет ответ пользователю
     * @param user - пользователь, который выполнил команду
     * @param chat - чат бота и пользователя
     * @param strings - аргументы, переданные с командой
     */
    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        Logger logger = Logger.getLogger(WorkupTrackerBotCommand.class);
        logger.info("User " + user.getUserName() + " start work with bot");
        callback.accept(user,chat);
    }
}
