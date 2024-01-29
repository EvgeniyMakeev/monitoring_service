package com.makeev.monitoring_service;

import com.makeev.monitoring_service.ui.ApplicationContext;

/**
 * The main class representing the utility meter reading application.
 * The application allows users to manage their utility meter readings, log in, and perform various actions.
 * @author Evgeniy Makeev
 * @version 1.4
 */
public class App {
    /**
     * The main method to start the application.
     *
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        new ApplicationContext().start();
    }
}