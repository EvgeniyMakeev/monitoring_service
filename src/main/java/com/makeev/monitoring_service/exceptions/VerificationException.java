package com.makeev.monitoring_service.exceptions;

public class VerificationException extends Exception {
    @Override
    public String getMessage() {
        return "Access is denied!\n"
                + "Wrong login or password.";
    }
}
