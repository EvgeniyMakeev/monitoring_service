package com.makeev.monitoring_service.servlets;

import com.makeev.monitoring_service.utils.InitDB;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class MyServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        new InitDB().initDB();
    }
}
