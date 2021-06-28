package com.example.demo;

public interface Coach {
    default String getDailyWorkout(){
        return ("Do nothing Coach.");
    }
}
