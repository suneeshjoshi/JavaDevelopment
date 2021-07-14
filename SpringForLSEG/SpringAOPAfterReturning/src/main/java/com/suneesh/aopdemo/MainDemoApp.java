package com.suneesh.aopdemo;

import com.suneesh.aopdemo.dao.AccountDAO;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

public class MainDemoApp {

    public static void main(String[] args) {

        // Get Annotation Based Context
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DemoConfig.class);

        // Get Bean
        AccountDAO accountDAO = context.getBean("accountDAO", AccountDAO.class);

        // Call Business Method
        accountDAO.addAccount();
        accountDAO.addAccount();
        accountDAO.addAccount();

        accountDAO.displayAccount();
        List<String> account = accountDAO.findAccount();

        System.out.println("Return from accountDAO.findAccount() =>");
        account.forEach(System.out::println);

        // Close Context
        context.close();


    }


}
