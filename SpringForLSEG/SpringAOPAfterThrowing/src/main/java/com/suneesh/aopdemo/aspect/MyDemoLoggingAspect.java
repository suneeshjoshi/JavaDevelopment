package com.suneesh.aopdemo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
public class MyDemoLoggingAspect {

    @Pointcut("execution(* com.suneesh.aopdemo.dao.*.findAccount(..))")
    private void sharePointcutExpression(){}

    @AfterThrowing(
            pointcut = "sharePointcutExpression()",
            throwing = "theException"
    )
    public void actionForBeforeAddAccount(JoinPoint joinPoint, Throwable theException){
        System.out.println(getClass() + "   ====================>>> Going to call *Account()" + joinPoint.getSignature() );
        System.out.println("exception = "+theException);
    }

}
