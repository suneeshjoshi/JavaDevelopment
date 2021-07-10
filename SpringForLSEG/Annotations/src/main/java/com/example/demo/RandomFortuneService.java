package com.example.demo;

import org.springframework.stereotype.Component;

@Component
public class RandomFortuneService implements FortuneService{

    @Override
    public String getDailyFortune() {
        return "Today is your RANDOM Lucky Day";
    }
}
