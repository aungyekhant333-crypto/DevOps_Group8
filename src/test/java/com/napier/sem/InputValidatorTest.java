package com.napier.sem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
        * Unit tests for the InputValidator utility class.
        * Verifies the acceptance of positive integers and rejection of invalid input.
 */
class InputValidatorTest {

    /**
            * Verifies that a valid positive integer is accepted.
            */
    @Test
    void shouldAcceptPositiveInteger() {
        assertEquals(10, InputValidator.parsePositiveInt("10"));
    }

    /**
            * Verifies that zero is rejected as it is not a positive integer.
            */
    @Test
    void shouldRejectZero() {
        assertEquals(-1, InputValidator.parsePositiveInt("0"));
    }

    /**
            * Verifies that negative integers are rejected.
            */
    @Test
    void shouldRejectNegativeInteger() {
        assertEquals(-1, InputValidator.parsePositiveInt("-5"));
    }

    /**
            * Verifies that alphabetic input is rejected.
            */
    @Test
    void shouldRejectAlphabeticInput() {
        assertEquals(-1, InputValidator.parsePositiveInt("hello"));
    }

    /**
            * Verifies that decimal numbers are rejected.
            */
    @Test
    void shouldRejectDecimalInput() {
        assertEquals(-1, InputValidator.parsePositiveInt("10.5"));
    }
}