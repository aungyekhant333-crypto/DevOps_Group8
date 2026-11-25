package com.napier.sem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CountryTest {

    @Test
    void shouldReturnCountryCode() {
        Country country = new Country(
                "MMR", "Myanmar", "Asia", "Southeast Asia", 54500000, 1
        );

        assertEquals("MMR", country.getCode());
    }

    @Test
    void shouldReturnCountryName() {
        Country country = new Country(
                "MMR", "Myanmar", "Asia", "Southeast Asia", 54500000, 1
        );

        assertEquals("Myanmar", country.getName());
    }

    @Test
    void shouldReturnContinent() {
        Country country = new Country(
                "MMR", "Myanmar", "Asia", "Southeast Asia", 54500000, 1
        );

        assertEquals("Asia", country.getContinent());
    }

    @Test
    void shouldReturnRegion() {
        Country country = new Country(
                "MMR", "Myanmar", "Asia", "Southeast Asia", 54500000, 1
        );

        assertEquals("Southeast Asia", country.getRegion());
    }

    @Test
    void shouldReturnPopulation() {
        Country country = new Country(
                "MMR", "Myanmar", "Asia", "Southeast Asia", 54500000, 1
        );

        assertEquals(54500000, country.getPopulation());
    }

    @Test
    void shouldReturnCapitalId() {
        Country country = new Country(
                "MMR", "Myanmar", "Asia", "Southeast Asia", 54500000, 1
        );

        assertEquals(1, country.getCapital());
    }
}