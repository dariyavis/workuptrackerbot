package com.workuptrackerbot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;

@Configuration
@ImportResource("classpath:beans.xml")
public class ConfigXML {

//    @Bean
//    public TelegramBotsApi telegramBotsApi() {
//       // ApiContextInitializer.init();
//        return new TelegramBotsApi();
//    }
}
