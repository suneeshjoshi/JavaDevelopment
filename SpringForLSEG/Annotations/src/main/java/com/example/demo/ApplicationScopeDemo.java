package com.example.demo;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationScopeDemo {

    public static void main(String[] args) {

        // Load Spring configuration File
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        // Retrieve the bean from spring container
        Coach myCoach = context.getBean("baseballCoach", Coach.class);

        Coach myCoach2 = context.getBean("baseballCoach", Coach.class);

        boolean result = (myCoach==myCoach2);

        // Call methods on teh bean
        System.out.println("Both objects are same = "+ result);

        System.out.println("myCoach = " + myCoach);

        System.out.println("myCoach2 = " + myCoach2);


        // close the context
        context.close();

    }

}

