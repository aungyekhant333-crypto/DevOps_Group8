package com.napier.sem;

public final class InputValidator {

    private InputValidator() {
        // Utility class; prevent instantiation.
    }

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