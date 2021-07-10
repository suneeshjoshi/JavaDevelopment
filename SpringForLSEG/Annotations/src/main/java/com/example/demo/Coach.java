package com.example.demo;

public interface Coach {
    default String getDailyWorkout(){
        return ("Do nothing Coach.");
    }
    default String getDailyFortune(){
        return ("Nothing Fortune.");
    }
    default String getEmailAddress(){ return "No Email Address";};
    default String getTeam(){ return "No Team";};
}
