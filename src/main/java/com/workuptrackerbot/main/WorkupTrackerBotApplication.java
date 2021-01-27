package com.workuptrackerbot.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ImportResource("classpath:beans.xml")
@ComponentScan(basePackages = {"com.workuptrackerbot"})
@EntityScan(basePackages = {"com.workuptrackerbot.entity"})
@EnableJpaRepositories(basePackages = {"com.workuptrackerbot.repository"})
@SpringBootApplication
public class WorkupTrackerBotApplication {

    private static final Logger log = LoggerFactory.getLogger(WorkupTrackerBotApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(WorkupTrackerBotApplication.class, args);
    }

}