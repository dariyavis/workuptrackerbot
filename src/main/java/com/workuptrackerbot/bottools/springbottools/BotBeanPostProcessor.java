package com.workuptrackerbot.bottools.springbottools;

import com.workuptrackerbot.bottools.commands.BotCommandHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.data.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

public class BotBeanPostProcessor implements BeanPostProcessor {

    @Autowired
    ApplicationContext context;
    @Autowired
    BeanFactory beanFactory;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.getAnnotation(Bot.class) == null) {
            return bean;
        }
        Bot annotation = beanClass.getAnnotation(Bot.class);
        Class<?> bot = annotation.getClass();
        try {
            for (Field botField : bot.getFields()) {
                Field beanField = beanClass.getField(botField.getName());
                beanField.setAccessible(true);
                ReflectionUtils.setField(beanField, bean, botField.get(botField));
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        //        Field[] declaredFields = beanClass.getDeclaredFields();
        //        for (Field field : declaredFields) {
        //            Bot annotation = field.getAnnotation(Bot.class);
        //            if (annotation != null) {
        //                Class<?> type = field.getType();
        //                Object beanForInjection = context.getBean(type);
        //                field.setAccessible(true);
        //                ReflectionUtils.setField(field, bean, beanForInjection);
        //            }
        //        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.getAnnotation(Bot.class) == null || !(bean instanceof CommandInterceptorable)) return bean;
        CommandInterceptorable ci = (CommandInterceptorable) bean;
        Map<String, Object> map = context.getBeansWithAnnotation(BotCommand.class);
        map.entrySet().forEach(item -> {
            //            if(item instanceof BotCommandHandler){
            BotCommandHandler itemValue = (BotCommandHandler) item.getValue();
            Class<?> commandClass = itemValue.getClass();
            ci.addCommand(commandClass.getAnnotation(BotCommand.class).command(), itemValue);
            //            }
        });


        //        SpringBot bot = (SpringBot) bean;
        //        context.getBeansWithAnnotation(BotCommand.class);


        return bean;
    }
}