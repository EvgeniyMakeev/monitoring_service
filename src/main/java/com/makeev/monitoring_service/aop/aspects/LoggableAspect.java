package com.makeev.monitoring_service.aop.aspects;

import com.makeev.monitoring_service.service.AdminService;
import com.makeev.monitoring_service.utils.ConnectionManagerImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class LoggableAspect {

    private final AdminService adminService = new AdminService(new ConnectionManagerImpl());
    @Pointcut("within(com/makeev/monitoring_service/aop/annotations/Loggable *) && execution(* *(..))")
    public void annotatedByLoggable() {
    }

    @Around("annotatedByLoggable()")
    public Object logging(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String message1 = "Calling method " + proceedingJoinPoint.getSignature();
        System.out.println(message1);
        adminService.addEvent(message1);
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();
        String message2 = "Execution of method " + proceedingJoinPoint.getSignature() +
        " finished. Execution time is " + (endTime - startTime) + " ms";
        System.out.println(message2);
        adminService.addEvent(message2);
        return result;
    }

}
