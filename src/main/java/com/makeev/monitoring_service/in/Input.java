package com.makeev.monitoring_service.in;

import com.makeev.monitoring_service.exceptions.MonthFormatException;
import com.makeev.monitoring_service.exceptions.YearFormatException;

import java.util.regex.Pattern;

/**
 * Implementation of the {@link Input} interface for handling user input in a console application.
 * @author Evgeniy Makeev
 * @version 1.4
 */
public class Input {

    private final static Pattern PATTERN_ONLY_DOUBLE = Pattern.compile("^\\d+(\\.\\d+)?$");
    private final static Pattern PATTERN_ONLY_YEAR = Pattern.compile("\\d{4}");
    private final static Pattern PATTERN_ONLY_MONTH = Pattern.compile("\\d{2}");

    /**
     * Reads and returns a double input from the user.
     *
     * @return The double input from the user.
     */
    public Double getDouble(String inputString) {
        if (!PATTERN_ONLY_DOUBLE.matcher(inputString).matches()) {
            throw new NumberFormatException();
        } else {
            return Double.parseDouble(inputString);
        }
    }

    public int getYear(String inputString) {
        if (!PATTERN_ONLY_YEAR.matcher(inputString).matches()) {
            throw new YearFormatException();
        }
        int result = Integer.parseInt(inputString);
        int minYear = 2000;
        int maxYear = 2100;
        if (result < minYear || result > maxYear) {
            throw new YearFormatException();
        } else {
            return result;
        }
    }
    public int getMonth(String inputString) {
        if (!PATTERN_ONLY_MONTH.matcher(inputString).matches()) {
            throw new MonthFormatException();
        }
        int result = Integer.parseInt(inputString);
        if (result <= 0 || result > 12) {
            throw new MonthFormatException();
        } else {
            return result;
        }
    }
}
