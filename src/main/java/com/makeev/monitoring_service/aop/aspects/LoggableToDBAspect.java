//package com.makeev.monitoring_service.aop.aspects;
//
//import com.makeev.monitoring_service.service.AdminService;
//import com.makeev.monitoring_service.utils.ConnectionManagerImpl;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//
//import java.util.Arrays;
//
//@Aspect
//public class LoggableToDBAspect {
//
//    AdminService adminService = new AdminService(new ConnectionManagerImpl());
//    @Pointcut("within(@com.makeev.monitoring_service.aop.annotations.LoggableToDB *) && execution(* *(..))")
//    public void annotatedByLoggableToDB() {
//    }
//
//    @After("annotatedByLoggableToDB()")
//    public void loggingToDB(JoinPoint joinPoint) {
//        String args = "No args";
//        try {
//            args = Arrays.toString(joinPoint.getArgs());
//        } finally {
//            String message = joinPoint.getSignature().getName() + args;
//            adminService.addEvent(message);
//        }
//    }
//
//}
