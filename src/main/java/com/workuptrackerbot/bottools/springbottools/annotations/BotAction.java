package com.workuptrackerbot.bottools.springbottools.annotations;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value= ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface BotAction {
    String path();
    boolean command() default false;
    boolean callback() default false;
}
