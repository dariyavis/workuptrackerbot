package com.workuptrackerbot.bottools.springbottools;

import com.workuptrackerbot.bottools.commands.BotCommandHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BotBeanPostProcessor implements BeanPostProcessor {

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
        if (beanClass.getAnnotation(Bot.class) == null || !(bean instanceof CommandInterceptorable)) return bean;
        CommandInterceptorable ci = (CommandInterceptorable) bean;
        Map<String, Object> map = context.getBeansWithAnnotation(BotCommand.class);
        //по хорошему нужно через reflections
        map.entrySet().forEach(item -> {
            if (item.getValue() instanceof BotCommandHandler) {
                BotCommandHandler itemValue = (BotCommandHandler) item.getValue();
                Class<?> commandClass = itemValue.getClass();
                ci.addCommand(commandClass.getAnnotation(BotCommand.class).command(), itemValue);
            }
        });
        return bean;
    }
}