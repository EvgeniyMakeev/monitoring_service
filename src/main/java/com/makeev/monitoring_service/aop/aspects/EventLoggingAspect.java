package com.makeev.monitoring_service.aop.aspects;

import com.makeev.monitoring_service.service.AdminService;
import com.makeev.monitoring_service.utils.ConnectionManagerImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class EventLoggingAspect {

    private final AdminService adminService;
    public EventLoggingAspect() {
        adminService = new AdminService(new ConnectionManagerImpl());
    }
    @Around("execution(@com.makeev.monitoring_service.aop.annotations.Loggable * *(String)) && args(message)")
    public Object logEvent(ProceedingJoinPoint joinPoint, String message) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        adminService.addEvent(methodName);
        return joinPoint.proceed(new Object[]{message});
    }
}
