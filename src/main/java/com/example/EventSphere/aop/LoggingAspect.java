package com.example.EventSphere.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    @Around("execution(* com.example.EventSphere.service.*.*(..))")
    public Object printLLog(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime=System.currentTimeMillis();
        Object outPut=joinPoint.proceed();
        long endTime=System.currentTimeMillis();
        long duration=endTime-startTime;
        String username;
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        if(auth!=null && auth.isAuthenticated()){
            username=auth.getName();
            if ("anonymousUser".equals(username)) {
                username = "anonymous";
            }
        }else{
            username="anonymous";
        }

        System.out.println("user: "+username+"\n"+
                "args: "+ Arrays.toString(joinPoint.getArgs())+"\n"+
                "method : "+joinPoint.getSignature().getName()+"\n"+"Duration: "+duration);
        return outPut;
    }
}
