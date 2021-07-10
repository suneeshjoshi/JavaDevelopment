package com.example.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;

//@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {

//		SpringApplication.run(DemoApplication.class, args);

		// Load Spring configuration File
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

		// Retrieve the bean from spring container
		Coach myCoach = context.getBean("baseballCoach", Coach.class);

		// Call methods on teh bean
		System.out.println(myCoach.getDailyWorkout());
		System.out.println(myCoach.getDailyFortune());
		System.out.println("Email Address = "+myCoach.getEmailAddress());
		System.out.println("Team Name  = "+myCoach.getTeam());

		// close the context
		context.close();

	}

}
