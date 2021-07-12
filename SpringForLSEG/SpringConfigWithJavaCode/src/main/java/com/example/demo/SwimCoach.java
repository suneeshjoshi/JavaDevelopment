package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class SwimCoach implements Coach{

    private FortuneService fortuneService;
    @Value("${foo.email}")
    private String emailAddress;

    @Value("${foo.team}")
    private String team;


    @PostConstruct
    public void myInitMethod(){
        System.out.println("Can be used to initialize.");
    }

    @PreDestroy
    public void myDestroyMethod(){
        System.out.println("Can be used to de-initialize.");
    }

    public SwimCoach() {
        System.out.println("SwimCoach() : Inside Constructor");
    }

    @Autowired
    public SwimCoach(FortuneService fortuneService) {
        System.out.println("SwimCoach() : Inside Parameterised Constructor");
        this.fortuneService = fortuneService;
    }

    @Override
    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        System.out.println("Inside Email Address Setter method");
        this.emailAddress = emailAddress;
    }

    @Override
    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        System.out.println("Inside Team Setter method");
        this.team = team;
    }

    @Override
    public String getDailyWorkout(){
        return ("Spend 30 minutes on Swimming practise.");
    }

    @Override
    public String getDailyFortune() {
        return fortuneService.getDailyFortune();
    }
}
