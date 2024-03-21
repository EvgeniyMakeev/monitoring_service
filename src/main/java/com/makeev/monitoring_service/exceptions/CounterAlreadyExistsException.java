package com.makeev.monitoring_service.exceptions;

/**
 * The {@code CounterAlreadyExistsException} class is an exception that is thrown when attempting to add a user with a counter
 * that already exists in the system. It extends the {@code Exception} class and provides a custom error message.
 */
public class CounterAlreadyExistsException extends RuntimeException {

    /**
     * Overrides the {@code getMessage} method to provide a custom error message for the counter already exists exception.
     *
     * @return A string representing the error message for the counter already exists exception.
     */
    @Override
    public String getMessage() {
        return "A counter already exists!";
    }
}
