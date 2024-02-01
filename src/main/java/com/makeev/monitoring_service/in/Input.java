package com.makeev.monitoring_service.in;

/**
 * An interface defining methods for user input in a console application.
 * @author Evgeniy Makeev
 * @version 1.4
 * */
public interface Input {

    /**
     * Reads and returns an integer input from the user within the specified range.
     *
     * @param max The maximum allowed integer value.
     * @return The integer input from the user.
     */
    int getInt(int max);

    /**
     * Reads and returns a string input from the user.
     *
     * @return The string input from the user.
     */
    String getString();

    /**
     * Reads and returns a double input from the user.
     *
     * @return The double input from the user.
     */
    Double getDouble();

    /**
     * Reads and returns an integer input from the user within the specified length and range.
     *
     * @param maxLength The maximum length of the input.
     * @param min       The minimum allowed integer value.
     * @param max       The maximum allowed integer value.
     * @return The integer input from the user.
     */
    Integer getInteger(int maxLength, int min, int max);
}
