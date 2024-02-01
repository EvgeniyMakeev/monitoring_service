package com.makeev.monitoring_service.exceptions;

public class EmptyException extends Exception{
    @Override
    public String getMessage() {
        return "There are no indications.";
    }
}
