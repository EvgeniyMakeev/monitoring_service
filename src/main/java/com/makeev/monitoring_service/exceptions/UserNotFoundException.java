package com.makeev.monitoring_service.exceptions;

/**
 * The {@code EmptyException} class is an exception that is thrown when there are no indications found.
 * It extends the {@code Exception} class and provides a custom error message.
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * Overrides the {@code getMessage} method to provide a custom error message for the exception.
     *
     * @return A string representing the error message for the empty exception.
     */
    @Override
    public String getMessage() {
        return "User is not found.";
    }
}
