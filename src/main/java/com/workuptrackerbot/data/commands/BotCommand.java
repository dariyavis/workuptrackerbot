package com.workuptrackerbot.data.commands;

import org.apache.log4j.Logger;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;

import java.util.function.BiConsumer;

@Deprecated
public class BotCommand {

    private BiConsumer<User, Chat> callback;

    public BotCommand(BiConsumer<User, Chat> callback) {
        this.callback = callback;
    }
    /**
     * реализованный метод класса BotCommand, в котором обрабатывается команда, введенная пользователем
     * @param absSender - отправляет ответ пользователю
     * @param user - пользователь, который выполнил команду
     * @param chat - чат бота и пользователя
     * @param strings - аргументы, переданные с командой
     */

    public void execute(User user, Chat chat) {
        Logger logger = Logger.getLogger(BotCommand.class);
        logger.info("User " + user.getUserName() + " start work with bot");
        callback.accept(user,chat);
    }
}
