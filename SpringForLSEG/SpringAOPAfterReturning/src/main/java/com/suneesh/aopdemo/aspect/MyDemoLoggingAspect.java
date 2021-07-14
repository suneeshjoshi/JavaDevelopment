package com.suneesh.aopdemo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class MyDemoLoggingAspect {

    @Pointcut("execution(* com.suneesh.aopdemo.dao.*.findAccount(..))")
    private void sharePointcutExpression(){}

    @AfterReturning(
            pointcut = "sharePointcutExpression()",
            returning = "returnValue"
    )
    public void actionForBeforeAddAccount(JoinPoint joinPoint, List<String> returnValue){
        System.out.println(getClass() + "   ====================>>> Going to call *Account()" + joinPoint.getSignature() );
        returnValue.forEach(System.out::println);
    }

}
