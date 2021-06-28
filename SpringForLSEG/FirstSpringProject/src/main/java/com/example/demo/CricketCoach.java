package com.example.demo;

public class CricketCoach implements Coach{
    private FortuneService fortuneService;

    // Spring Injection : Constructor Injection
    public CricketCoach(FortuneService fortuneService) {
        this.fortuneService = fortuneService;
    }

    @Override
    public String getDailyWorkout(){
        return ("Spend 30 minutes on Cricket practise");
    }
}
