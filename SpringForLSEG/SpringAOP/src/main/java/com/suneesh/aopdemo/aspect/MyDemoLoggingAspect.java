package com.suneesh.aopdemo.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyDemoLoggingAspect {

//    @Before("execution(public void addAccount())")
    @Before("execution(* com.suneesh.aopdemo.*.*.*Account(..))")
    public void actionForBeforeAddAccount(){
        System.out.println(getClass() + "   ====================>>> Going to call *Account()");
    }

}
