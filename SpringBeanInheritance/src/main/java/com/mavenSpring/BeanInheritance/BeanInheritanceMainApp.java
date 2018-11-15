package com.mavenSpring.BeanInheritance;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 *
 */
public class BeanInheritanceMainApp 
{
    public static void main( String[] args )
    {
        ApplicationContext context = new ClassPathXmlApplicationContext("beanInheritance.xml");
        Book b = (Book)context.getBean("book");
        Book eb = (Book)context.getBean("ebook");
        
        System.out.println(b);
        System.out.println(eb);
        
    }
}
