package com.example.demo;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JavaConfigDemoApp {

    public static void main(String[] args) {

        // Read Spring Config class
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SportConfig.class);

        // Retrieve the bean from spring container
        Coach myCoach = context.getBean("swimCoach", Coach.class);

        // Call methods on teh bean
        System.out.println(myCoach.getDailyWorkout());

        System.out.println(myCoach.getDailyFortune());

        System.out.println("Email Address = "+myCoach.getEmailAddress());
        System.out.println("Team Name  = "+myCoach.getTeam());

        // close the context
        context.close();

    }

}

