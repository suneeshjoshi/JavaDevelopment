package com.example.demo;

public class HappyFortuneService implements FortuneService{

    @Override
    public String getDailyFortune() {
        return "Today is your Lucky Day";
    }
}
