package com.workuptrackerbot.bottools.springbottools.bpp;

import com.workuptrackerbot.bottools.springbottools.annotations.*;
import com.workuptrackerbot.bottools.springbottools.callbackquery.BotStateInteroperable;
import com.workuptrackerbot.bottools.springbottools.commands.ActionState;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class BotStatePostProcessor implements BeanPostProcessor {

    @Autowired
    ApplicationContext context;
    @Autowired
    BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(@Nonnull Object bean, @Nonnull String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(@Nonnull Object bean, @Nonnull String beanName) throws BeansException {

        Class<?> beanClass = bean.getClass();
        if (beanClass.getAnnotation(Bot.class) == null || !(bean instanceof BotStateInteroperable)) return bean;
        BotStateInteroperable ci = (BotStateInteroperable) bean;

        Map<String, Object> mapCQuerys = context.getBeansWithAnnotation(HasBotAction.class);
        mapCQuerys.entrySet().forEach(item -> {
            Object value = item.getValue();
            Class<?> beanClassItem = value.getClass();
            Method[] methods = beanClassItem.getDeclaredMethods();
            for (Method method : methods) {
                BotAction annotation = method.getAnnotation(BotAction.class);
                if (annotation != null) {
                    if (annotation.command()) {
                        ci.addCommand(annotation.path(), (botApiMethodConsumer, update) -> {
                                    try {
                                        return (ActionState) method.invoke(value, botApiMethodConsumer, update);
                                    } catch (IllegalAccessException | InvocationTargetException e) {
                                        e.printStackTrace();
                                    }
                                    return null;
                                }
                        );
                    }
                    if (annotation.callback()) {
                        ci.addCallback(annotation.path(), (botApiMethodConsumer, update) -> {
                                    try {
                                        return (ActionState) method.invoke(value, botApiMethodConsumer, update);
                                    } catch (IllegalAccessException | InvocationTargetException e) {
                                        e.printStackTrace();
                                    }
                                    return null;
                                }
                        );
                    }
                    if (!annotation.command() && !annotation.callback()) {
                        ci.addBotState(annotation.path(), (botApiMethodConsumer, update) -> {
                                    try {
                                        return (ActionState) method.invoke(value, botApiMethodConsumer, update);
                                    } catch (IllegalAccessException | InvocationTargetException e) {
                                        e.printStackTrace();
                                    }
                                    return null;
                                }
                        );
                    }
                }
            }
        });
        return bean;
    }
}