package com.workuptrackerbot.bottools.springbottools.bpp;

import com.workuptrackerbot.bottools.springbottools.annotations.MaxIndexCommand;
import com.workuptrackerbot.bottools.springbottools.commands.Command;
import com.workuptrackerbot.bottools.springbottools.annotations.Answer;
import com.workuptrackerbot.bottools.springbottools.annotations.BotCommand;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNullApi;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class BotCommandBeanPostProcessor implements BeanPostProcessor {

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
        if (beanClass.getAnnotation(BotCommand.class) == null || !(bean instanceof Command)) return bean;
        Map<Integer, Function<Message, BotApiMethod>> steps = new HashMap<>();

        Method[] methods = beanClass.getDeclaredMethods();
        for (Method method : methods) {
            Answer annotation = method.getAnnotation(Answer.class);
            if(annotation != null) {
                steps.put(annotation.index(), o -> {
                    try {
                        return (BotApiMethod) method.invoke(bean, o);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                });
            }
        }
        Command command = (Command) bean;
        command.setAnswers(steps);

        Field[] fields = beanClass.getSuperclass().getDeclaredFields();
        for (Field field : fields) {
            if(field.getAnnotation(MaxIndexCommand.class) != null) {
                field.setAccessible(true);
                try {
                    field.set(bean, steps.size());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }
}