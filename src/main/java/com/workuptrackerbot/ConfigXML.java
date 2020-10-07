package com.workuptrackerbot;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:beans.xml")
public class ConfigXML {

    //    @Bean
    //    public TelegramBotsApi telegramBotsApi() {
    //       // ApiContextInitializer.init();
    //        return new TelegramBotsApi();
    //    }
}
