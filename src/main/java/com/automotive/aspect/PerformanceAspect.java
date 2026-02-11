package com.automotive.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@Order(2)
public class PerformanceAspect {
    
    @Around("@annotation(com.automotive.annotation.TrackTime)")
    public Object trackTime (ProceedingJoinPoint jp) throws Throwable {
       
        long start = System.nanoTime();
        Object result = jp.proceed();
       
        long ms = (System.nanoTime() - start) / 1_000_000;
        log.info("[@TrackTime] {}() took {} ms", jp.getSignature().getName(), ms);
       
        return result;
    }
}
