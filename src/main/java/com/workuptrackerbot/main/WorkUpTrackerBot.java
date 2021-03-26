package com.workuptrackerbot.main;

import com.workuptrackerbot.bottools.springbottools.SpringBot;
import com.workuptrackerbot.bottools.springbottools.annotations.Bot;
import com.workuptrackerbot.bottools.springbottools.annotations.BotAction;
import com.workuptrackerbot.bottools.springbottools.annotations.HasBotAction;
import com.workuptrackerbot.bottools.springbottools.commands.ActionState;
import com.workuptrackerbot.bottools.tlgmtools.MessageTools;
import com.workuptrackerbot.bottools.tlgmtools.ReplyKeyboardTools;
import com.workuptrackerbot.entity.Interval;
import com.workuptrackerbot.service.ActionStateService;
import com.workuptrackerbot.service.IntervalService;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.function.Consumer;

@Bot
@HasBotAction
public class WorkUpTrackerBot extends SpringBot {

    @Value("${bot.token}")
    private String botToken;
    @Value("${bot.username}")
    private String botUsername;

    @Autowired
    private Properties properties;

    @Autowired
    private IntervalService intervalService;

    @Autowired
    private ActionStateService actionStateService;

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @PostConstruct
    public void postConstract() {
        StringBuilder builder = new StringBuilder();
        builder.append("BOT: ");
        builder.append("username = {} ");
        builder.append("token = {}");
        logger.info(builder.toString(), botUsername, botToken);
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void saveActionState(ActionState actionState) {
        actionStateService.saveActionState(actionState);
    }

    @Override
    public ActionState getActionState(User user) {
        return actionStateService.getActionState(user);
    }


    @Override
    public void onUpdateReceivedMessage(Update update) {
        Message message = update.getMessage();
        User user = message.getFrom();
        Interval interval = intervalService.createInterval(user, message.getText(), convertDate(message.getDate()));
        deleteMessage(message.getChatId().toString(), message.getMessageId());
        if (interval == null) {
            return;
        }

//        deleteMessage(message.getChatId().toString(), message.getMessageId());

        executeMessage(message.getChatId(),
                MessageFormat.format(
                        properties.getProperty("keyboard.tracking.start.message"),
                        interval.getUserProject().getProject().getName()),
                ReplyKeyboardTools.createInlineKeyboardMarkupWithPath(
                        new ReplyKeyboardTools.ButtonWithPath(
                                MessageFormat.format(
                                        properties.getProperty("keyboard.tracking.stop.button"),
                                        interval.getUserProject().getProject().getName()),
                                "stopTracking",
                                interval.getId().toString()
                        )));
    }

    //from unix
    @Override
    public void onUpdateReceivedCallbackQuery(Update update) {
//        CallbackQuery callbackQuery = update.getCallbackQuery();
//        Interval interval = intervalService.updateInterval(
//                callbackQuery.getData(),
//                new Timestamp(System.currentTimeMillis()));
//
//        Message message = update.getCallbackQuery().getMessage();
//        deleteMessage(message.getChatId().toString(), message.getMessageId());
//
//
////       DateFormat dateFormat = new SimpleDateFormat("HH:mm  dd.MM.yy");
//       DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//
//
//        executeMessage(callbackQuery.getMessage().getChatId(),
//                MessageFormat.format(
//                        properties.getProperty("keyboard.tracking.stop.message_interval"),
//                        interval.getUserProject().getProject().getName(),
//                        dateFormat.format(interval.getStartDate()),
//                        dateFormat.format(interval.getStopDate()),
//                        periodToString(interval.getStartDate(), interval.getStopDate())),
//                null);

    }

    @BotAction(path = "stopTracking", callback = true)
    public String stopTracking(Consumer<BotApiMethod> execute, Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Interval interval = intervalService.updateInterval(
                callbackQuery.getData(),
                new Timestamp(System.currentTimeMillis()));

        Message message = update.getCallbackQuery().getMessage();
        execute.accept(MessageTools.deleteMessage(message.getChatId().toString(), message.getMessageId()));


//       DateFormat dateFormat = new SimpleDateFormat("HH:mm  dd.MM.yy");
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.enableMarkdown(true);
        sendMessage.setText(MessageFormat.format(
                properties.getProperty("keyboard.tracking.stop.message_interval"),
                interval.getUserProject().getProject().getName(),
                dateFormat.format(interval.getStartDate()),
                dateFormat.format(interval.getStopDate()),
                periodToString(interval.getStartDate(), interval.getStopDate())));
        sendMessage.setReplyMarkup(
                ReplyKeyboardTools.createInlineKeyboardMarkupWithPath(
                        new ReplyKeyboardTools.ButtonWithPath(
                                MessageFormat.format(
                                        properties.getProperty("keyboard.tracking.removeinterval.button"),
                                        interval.getUserProject().getProject().getName()),
                                "removeInterval",
                                interval.getId().toString()
                        )));
        execute.accept(sendMessage);
        return null;
    }

    @BotAction(path = "removeInterval", callback = true)
    public String removeInterval(Consumer<BotApiMethod> execute, Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
        sendMessage.enableMarkdown(true);
        sendMessage.setText(properties.getProperty("keyboard.tracking.removedinterval.button"));
        intervalService.removeInteval(update.getCallbackQuery().getData());
        execute.accept(sendMessage);
        return null;
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

    public static Timestamp convertDate(Integer javaTimeStamp) {
        return new Timestamp((long) javaTimeStamp * 1000);
    }

    private void executeMessage(Long chatId, String text, ReplyKeyboard replyKeyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        message.setReplyMarkup(replyKeyboard);
        message.enableMarkdown(true);
        try {
            this.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


}
