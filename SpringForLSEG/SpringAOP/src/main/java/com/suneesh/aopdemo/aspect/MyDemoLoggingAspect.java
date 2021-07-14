package com.suneesh.aopdemo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class MyDemoLoggingAspect {

    @Pointcut("execution(* com.suneesh.aopdemo.*.*.*Account(..))")
    private void sharePointcutExpression(){}

//    @Before("execution(public void addAccount())")
    @Before("sharePointcutExpression()")
    public void actionForBeforeAddAccount(JoinPoint joinPoint){
        System.out.println(getClass() + "   ====================>>> Going to call *Account()" + joinPoint.getSignature() );
        for(Object arg : joinPoint.getArgs()){
            System.out.println(arg);
        }
    }

}
