package com.example.demo;

public interface FortuneService {

    default String getDailyFortune(){
        return "No Fortune Service provided today.";
    }
}
