package com.napier.sem;

/**
        * Utility class for validating positive integer input.
        * Converts valid positive integer strings into integers and rejects invalid values.
 */
public final class InputValidator {

    /**
            * Private constructor to prevent instantiation of this utility class.
            */
    private InputValidator() {
        // Utility class; prevent instantiation.
    }

    /**
            * Parses a string and checks whether it represents a positive integer.
     *
             * @param input the string to validate
     * @return the parsed positive integer, or -1 if the input is invalid
     *         or the number is zero or negative
     */
    public static int parsePositiveInt(String input) {
        try {
            int number = Integer.parseInt(input.trim());

            if (number <= 0) {
                return -1;
            }

            return number;

        } catch (NumberFormatException e) {
            return -1;
        }
    }
}