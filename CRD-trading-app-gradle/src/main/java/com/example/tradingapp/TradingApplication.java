package com.example.tradingapp;


import com.example.tradingapp.model.Trade;
import com.example.tradingapp.repository.TradeRepository;
import com.example.tradingapp.service.TradeService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

@SpringBootApplication
@EnableCaching
public class TradingApplication {
    public static void main(String[] args) {
        SpringApplication.run(TradingApplication.class, args);
    }
    @Bean
    ApplicationRunner init(TradeRepository repository, TradeService tradeService) {
        return args -> {
            IntStream.range(1, 101).forEach(i -> {
                IntStream.range(1, 101).forEach(v -> {
                    Trade trade = new Trade(null, "T" + i, v, "Counterparty " + i, i * 1000.0, LocalDateTime.now(),
                            String.format("{\"tradeId\":\"T%d\", \"version\":%d, \"counterparty\":\"Counterparty %d\", \"amount\":%.2f}", i, v, i, i * 1000.0));
                    repository.save(trade);
                });
            });
            tradeService.loadAllTradesIntoCache();
        };
    }
}