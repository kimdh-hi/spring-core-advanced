package com.aop.lab3.internalcall.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class CallLogAspect {

    @Before("execution(* com.aop.lab3.internalcall.*..*(..))")
    public void doLog(JoinPoint joinPoint) {
        log.info("call logAop: {}", joinPoint.getSignature());
    }
}
