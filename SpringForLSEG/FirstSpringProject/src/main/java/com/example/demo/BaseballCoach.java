package com.example.demo;

import java.util.concurrent.ForkJoinPool;

public class BaseballCoach implements Coach{

    private FortuneService fortuneService;

    // Spring Injection : Constructor Injection
    public BaseballCoach(FortuneService fortuneService) {
        this.fortuneService = fortuneService;
    }

    @Override
    public String getDailyWorkout(){
        return ("Spend 30 minutes on Batting practise.");
    }

    @Override
    public String getDailyFortune() {
        return fortuneService.getDailyFortune();
    }
}
