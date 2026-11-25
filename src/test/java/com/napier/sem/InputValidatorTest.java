package com.napier.sem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InputValidatorTest {

    @Test
    void shouldAcceptPositiveInteger() {
        assertEquals(10, InputValidator.parsePositiveInt("10"));
    }

    @Test
    void shouldRejectZero() {
        assertEquals(-1, InputValidator.parsePositiveInt("0"));
    }

    @Test
    void shouldRejectNegativeInteger() {
        assertEquals(-1, InputValidator.parsePositiveInt("-5"));
    }

    @Test
    void shouldRejectAlphabeticInput() {
        assertEquals(-1, InputValidator.parsePositiveInt("hello"));
    }

    @Test
    void shouldRejectDecimalInput() {
        assertEquals(-1, InputValidator.parsePositiveInt("10.5"));
    }
}