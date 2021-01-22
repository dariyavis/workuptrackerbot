package com.workuptrackerbot.main;

import com.workuptrackerbot.bottools.springbottools.commands.CommandState;
import com.workuptrackerbot.bottools.springbottools.annotations.Bot;
import com.workuptrackerbot.bottools.springbottools.SpringBot;
import com.workuptrackerbot.bottools.tlgmtools.ReplyKeyboardTools;
import com.workuptrackerbot.entity.Interval;
import com.workuptrackerbot.service.IntervalService;
import org.joda.time.Period;
import org.joda.time.ReadablePeriod;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.api.objects.*;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.lang.invoke.MethodHandles;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.function.Function;

@Bot
public class WorkUpTrackerBot extends SpringBot {

    @Autowired
    private Properties properties;

    @Autowired
    private IntervalService intervalService;

    private CommandState commandStates = null;

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    @Override
    public String getBotUsername() {
        return properties.getProperty("bot.username");
    }

    @Override
    public String getBotToken() {
        return properties.getProperty("bot.token");
    }

    @Override
    public void saveCommandState(CommandState commandState) {
        this.commandStates = commandState;
    }

    @Override
    public CommandState getCommandState(User user, Chat chat) {
        //todo тянет из базы если есть
        return commandStates;
    }


    @Override
    public void onUpdateReceivedMessage(Update update) {
        Message message = update.getMessage();
        User user = message.getFrom();
        Interval interval = intervalService.createInterval(user, message.getText(), convertDate(message.getDate()));
        if (interval == null) {
            return;
        }

        deleteMessage(message.getChatId().toString(), message.getMessageId());

        //todo сделать кнопку check времени, которая будет удалять сообщение через 30 секунд
        executeMessage(message.getChatId(),
                MessageFormat.format(
                properties.getProperty("keyboard.tracking.start.message"),
                        interval.getUserProject().getProject().getName()),
                ReplyKeyboardTools.createInlineKeyboard(interval,
                        o -> properties.getProperty("keyboard.tracking.stop.button"),
                        o -> ((Interval) o).getId().toString()));
    }

    //from unix
    public static Timestamp convertDate(Integer javaTimeStamp) {
         return new Timestamp((long)javaTimeStamp * 1000);
    }

    @Override
    public void onUpdateReceivedCallbackQuery(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Interval interval = intervalService.updateInterval(
                callbackQuery.getData(),
                new Timestamp(System.currentTimeMillis()));

        Message message = update.getCallbackQuery().getMessage();
        deleteMessage(message.getChatId().toString(), message.getMessageId());


       DateFormat dateFormat = new SimpleDateFormat("HH:mm  dd.MM.yy");
//       DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


        executeMessage(callbackQuery.getMessage().getChatId(),
                MessageFormat.format(
                        properties.getProperty("keyboard.tracking.stop.message_interval"),
                        interval.getUserProject().getProject().getName(),
                        dateFormat.format(interval.getStartDate()),
                        dateFormat.format(interval.getStopDate()),
                        periodToString(interval.getStartDate(), interval.getStopDate())),
                null);

    }

    private String periodToString(Timestamp startDate, Timestamp stopDate) {

        Period period = new Period(startDate.getTime(), stopDate.getTime());

        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendYears().appendSuffix("y ")
                .appendMonths().appendSuffix("m ")
                .appendWeeks().appendSuffix(" week, ", " weeks, ")
                .appendDays().appendSuffix("d ")
                .appendHours().appendSuffix("h ")
                .appendMinutes().appendSuffix("min ")
                .appendSeconds().appendSuffix("s ")
                .printZeroNever()
                .toFormatter();

        return formatter.print(period);
    }

    private void executeMessage(Long chatId, String text, ReplyKeyboard replyKeyboard){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(replyKeyboard);
        message.enableMarkdown(true);
        try {
            this.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    private void deleteMessage(String chatId, Integer messageId) {
        DeleteMessage deleteMessage = new DeleteMessage();
        deleteMessage.setChatId(chatId);
        deleteMessage.setMessageId(messageId);
        try {
            this.execute(deleteMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


}
