package com.example.demo;

public class CricketCoach implements Coach{
    private FortuneService fortuneService;
    private String emailAddress;
    private String team;

    public CricketCoach() {
        System.out.println("CricketCoach() : Inside Constructor");
    }

    // Spring Injection : Setter Injection
    public void setFortuneService(FortuneService fortuneService){
        System.out.println("Inside Fortune Service Setter method");
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
        return ("Spend 30 minutes on Cricket practise");
    }
}
