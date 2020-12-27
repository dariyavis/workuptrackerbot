package com.workuptrackerbot.bottools.springbottools.bpp;

import com.workuptrackerbot.bottools.commands.Command;
import com.workuptrackerbot.bottools.springbottools.annotations.Answer;
import com.workuptrackerbot.bottools.springbottools.annotations.BotCommand;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.api.methods.BotApiMethod;
import org.telegram.telegrambots.api.objects.Message;

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
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
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
        return bean;
    }
}