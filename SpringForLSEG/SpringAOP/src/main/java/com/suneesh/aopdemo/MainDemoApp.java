package com.suneesh.aopdemo;

import com.suneesh.aopdemo.dao.AccountDAO;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
        // Close Context
        context.close();


    }


}
