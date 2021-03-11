package com.workuptrackerbot.bottools.springbottools.bpp;

import com.workuptrackerbot.bottools.springbottools.callbackquery.CallbackQueryInterceptorable;
import com.workuptrackerbot.bottools.springbottools.commands.CommandInterceptorable;
import com.workuptrackerbot.bottools.springbottools.annotations.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;

import javax.annotation.Nonnull;
import java.lang.reflect.Method;
import java.util.Map;

public class CallbackBeanPostProcessor implements BeanPostProcessor {

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
        if (beanClass.getAnnotation(Bot.class) == null || !(bean instanceof CommandInterceptorable)) return bean;
        CallbackQueryInterceptorable ci = (CallbackQueryInterceptorable) bean;


        Map<String, Object> mapCQuerys = context.getBeansWithAnnotation(com.workuptrackerbot.bottools.springbottools.annotations.HasCallbackQuery.class);
        //по хорошему нужно через reflections
        mapCQuerys.entrySet().forEach(item -> {
            Object value = item.getValue();
            Class<?> beanClassItem = value.getClass();
            String prefix = value.getClass().getAnnotation(HasCallbackQuery.class).prefix();
            Method[] methods = beanClassItem.getDeclaredMethods();
            for (Method method : methods) {
                CallbackQueryHandler annotation = method.getAnnotation(CallbackQueryHandler.class);
                if(annotation != null) {
                    ci.addCallBackQuery(prefix + annotation.path(), update -> {
                        try {
                            return (BotApiMethod) method.invoke(value, update);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    });
                }
            }
        });
        return bean;
    }
}