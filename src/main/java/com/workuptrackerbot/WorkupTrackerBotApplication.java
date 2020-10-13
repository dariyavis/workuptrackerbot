package com.workuptrackerbot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@RestController
public class WorkupTrackerBotApplication {

    private static final Logger log = LoggerFactory.getLogger(WorkupTrackerBotApplication.class.getName());

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(WorkupTrackerBotApplication.class, args);
    }

    @GetMapping("/hello")
    public String sayHello(@RequestParam(value = "myName", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }
}