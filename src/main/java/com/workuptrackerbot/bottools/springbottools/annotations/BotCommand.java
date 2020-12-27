package com.workuptrackerbot.bottools.springbottools.annotations;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Indexed;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value= ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface BotCommand {
    String command();
}
