package com.mavenSpring.SpringProject1;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Hello world!
 *
 */
public class EmployeeMainApp 
{
    public static void main( String[] args )
    {
    	
//    	Employee e = new Employee("Suneesh", "My address", 33);
//    	System.out.println(e);
 
    	Resource resource = new ClassPathResource("employee.xml");
    	BeanFactory factory = new XmlBeanFactory(resource);
    	Employee beanFactoryObject1 = (Employee) factory.getBean("emp1");
    	System.out.println(beanFactoryObject1);
    	
    	
    	ApplicationContext context = new ClassPathXmlApplicationContext("employee.xml");
    	Employee empBean = (Employee)context.getBean("emp1");
    	System.out.println(empBean);
    	
    	
    	Employee empBean2 = (Employee)context.getBean("emp2");
    	System.out.println(empBean2);

    	// Constructor Autowiring
    	Employee empBean3 = (Employee)context.getBean("emp3");
    	System.out.println(empBean3);

    	// Constructor Autowiring
    	Employee empBean4 = (Employee)context.getBean("emp4");
    	System.out.println(empBean4);
    	
    	
    	((AbstractApplicationContext) context).close();
    	
    }
}
