package com.example;

import com.example.processor.TradeProcessor;
import com.example.service.EventService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EventServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(EventServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner run(ApplicationContext context) {
        return args -> {
            EventService eventService = context.getBean(EventService.class);

            eventService.publishExecuteEvent("T123", 100);
            eventService.publishUnwindEvent("T123", 50);
            eventService.publishUpsizeEvent("T123", 75);

            TradeProcessor tradeProcessor = context.getBean(TradeProcessor.class);
            tradeProcessor.getTradeEventHistory("T123");
        };
    }
}
