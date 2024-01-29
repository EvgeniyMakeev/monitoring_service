package com.makeev.monitoring_service.in;

import java.util.Scanner;

/**
 * Implementation of the {@link Input} interface for handling user input com.makeev.in a console application.
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
        //option that user entered
        String optionString;

        //option that will convert for the system
        int option = -1;

        do {
            optionString = input.nextLine();

            if (optionString.matches("[0-" + max + "]+") && optionString.length() <= 1) {
                option = Integer.parseInt(optionString);
            } else {
                System.out.println("Enter only digits 0 - " + max);
            }
        } while (!optionString.matches("[0-" + max + "]+") || optionString.length() > 1);
        return option;
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
        double result = -1;
        boolean scan = true;
        while (scan) {
            str = input.nextLine();
            result = Double.parseDouble(str);
                if (result >= 0) {
                    scan = false;
                }
            }
        return result;
    }

    /**
     * Reads and returns an integer input from the user within the specified length and range.
     *
     * @param maxLength The maximum length of the input.
     * @param min       The minimum allowed integer value.
     * @param max       The maximum allowed integer value.
     * @return The integer input from the user.
     */
    @Override
    public Integer getInteger(int maxLength, int min, int max) {
        String str = "";
        int result = -1;
        boolean scan = true;
            while (scan) {
                str = input.nextLine();
                if (str.length() == maxLength) {
                    result = Integer.parseInt(str);
                    if (result >= min && result <= max) {
                        scan = false;
                    }
                }
            }
        return result;
    }

}
