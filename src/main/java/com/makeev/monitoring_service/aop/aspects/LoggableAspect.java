package com.makeev.monitoring_service.aop.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import com.makeev.monitoring_service.aop.annotations.Loggable;

@Aspect
public class LoggableAspect {

    @Pointcut("@annotation(loggable)")
    public void loggableMethods(Loggable loggable) {}

    @Around(value = "loggableMethods(loggable)", argNames = "proceedingJoinPoint,loggable")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint, Loggable loggable) throws Throwable {
        System.out.println("Calling method " + proceedingJoinPoint.getTarget().getClass().getSimpleName() +
                "." + proceedingJoinPoint.getSignature().getName());
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();
        System.out.println("Execution of method " + proceedingJoinPoint.getSignature() +
                " finished. Execution time is " + (endTime - startTime) + " ms");
        return result;
    }
}
