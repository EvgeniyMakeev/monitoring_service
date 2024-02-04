package com.makeev.monitoring_service.in;

import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Implementation of the {@link Input} interface for handling user input in a console application.
 * @author Evgeniy Makeev
 * @version 1.4
 */
public class InputImpl implements Input {

    /**
     * Service for input stream.
     */
    private final Scanner input = new Scanner(System.in);

    /**
     * Reads and returns an integer input from the user within the specified range.
     *
     * @param max The maximum allowed integer value.
     * @return The integer input from the user.
     */
    @Override
    public int getInt(int max) {
        String str = "";
        String regex = "^\\d+$";
        Pattern pattern = Pattern.compile(regex);
        int option = -1;
        do {
            str = input.nextLine();
            if (pattern.matcher(str).matches()) {
                option = Integer.parseInt(str);
                if (option > max | option <= 0) {
                    System.out.println("Enter only digits 0 - " + max);
                } else {
                    return option;
                }
            }
        } while (true);
    }

    /**
     * Reads and returns a string input from the user.
     *
     * @return The string input from the user.
     */
    @Override
    public String getString() {
        return input.next();
    }

    /**
     * Reads and returns a double input from the user.
     *
     * @return The double input from the user.
     */
    @Override
    public Double getDouble() {
        String str = "";
        String regex = "^\\d+(\\.\\d+)?$";
        Pattern pattern = Pattern.compile(regex);
        while (!pattern.matcher(str).matches()) {
            str = input.next();
        }
        return Double.parseDouble(str);
    }

    /**
     * Reads and returns an integer input from the user within the specified length and range.
     *
     * @param maxLength The maximum length of the input.
     * @return The integer input from the user.
     */
    @Override
    public Integer getInteger(int maxLength) {
        String str = "";
        String regex = "\\d{" + maxLength + "}";
        Pattern pattern = Pattern.compile(regex);
            while (!pattern.matcher(str).matches()) {
                str = input.next();
            }
        return Integer.parseInt(str);
    }

}
