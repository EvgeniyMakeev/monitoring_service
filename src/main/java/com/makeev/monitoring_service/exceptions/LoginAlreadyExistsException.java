package com.makeev.monitoring_service.exceptions;

public class LoginAlreadyExistsException extends Exception {
    @Override
    public String getMessage() {
        return "A user with this login already exists!";
    }
}
