package com.makeev.monitoring_service.exceptions;

public class IncorrectValuesException extends Exception {
    @Override
    public String getMessage() {
        return "Meter readings have not been sent.\n" +
                "Testimony can be submitted only once a month\n" +
                "and it cannot be less than the current one.";
    }
}
