package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
//@Scope("prototype")
public class BaseballCoach implements Coach{

    @Autowired
    @Qualifier("randomFortuneService")
    private FortuneService fortuneService;
    private String emailAddress;
    private String team;



    public BaseballCoach() {
        System.out.println("BaseballCoach() : Inside Constructor");
    }


//    // Here Autowired is optional only if the below CTOR is the ONLY CTOR present for teh class.
//    // If more than ONE CTOR present, then '@Autowired' needs to be provided
////    @Autowired
//    public BaseballCoach(FortuneService fortuneService) {
//        System.out.println("BaseballCoach() : Inside Parameterised Constructor");
//        this.fortuneService = fortuneService;
//    }

//    // Spring Injection : Setter Injection
//    @Autowired
//    public void setFortuneService(FortuneService fortuneService){
//        System.out.println("Inside Fortune Service Setter method");
//        this.fortuneService = fortuneService;
//    }

//    // Spring Injection : Method Injection
//    @Autowired
//    public void doSomeThingMethod(FortuneService fortuneService){
//        System.out.println("Inside Fortune Service Setter method");
//        this.fortuneService = fortuneService;
//    }

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
        return ("Spend 30 minutes on Batting practise.");
    }

    @Override
    public String getDailyFortune() {
        return fortuneService.getDailyFortune();
    }
}
