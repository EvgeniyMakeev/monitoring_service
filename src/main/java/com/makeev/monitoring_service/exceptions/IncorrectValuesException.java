package com.makeev.monitoring_service.exceptions;

/**
 * The {@code IncorrectValuesException} class is an exception that is thrown when meter readings have not been sent,
 * or when testimony is attempted to be submitted more than once a month or with a value less than the current one.
 * It extends the {@code Exception} class and provides a custom error message.
 */
public class IncorrectValuesException extends RuntimeException {

    /**
     * Overrides the {@code getMessage} method to provide a custom error message for the incorrect values exception.
     *
     * @return A string representing the error message for the incorrect values exception.
     */
    @Override
    public String getMessage() {
        return """
                Meter readings have not been sent.
                Testimony can be submitted only once a month
                and it cannot be less than the current one.
                """;
    }
}
