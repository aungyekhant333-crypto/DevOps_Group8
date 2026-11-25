package com.napier.sem;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PopulationCalculatorTest {

    @Test
    void shouldCalculateNormalPopulationBreakdown() {
        PopulationCalculator.Breakdown result =
                PopulationCalculator.calculate(1_000_000, 750_000);

        assertEquals(1_000_000, result.totalPopulation());
        assertEquals(750_000, result.urbanPopulation());
        assertEquals(250_000, result.nonUrbanPopulation());
        assertEquals(75.0, result.urbanPercentage(), 0.001);
        assertEquals(25.0, result.nonUrbanPercentage(), 0.001);
    }

    @Test
    void shouldHandleZeroPopulation() {
        PopulationCalculator.Breakdown result =
                PopulationCalculator.calculate(0, 0);

        assertEquals(0, result.totalPopulation());
        assertEquals(0, result.urbanPopulation());
        assertEquals(0, result.nonUrbanPopulation());
        assertEquals(0.0, result.urbanPercentage());
        assertEquals(0.0, result.nonUrbanPercentage());
    }

    @Test
    void shouldHandleNoUrbanPopulation() {
        PopulationCalculator.Breakdown result =
                PopulationCalculator.calculate(1_000_000, 0);

        assertEquals(1_000_000, result.nonUrbanPopulation());
        assertEquals(0.0, result.urbanPercentage());
        assertEquals(100.0, result.nonUrbanPercentage());
    }

    @Test
    void shouldHandleEntirelyUrbanPopulation() {
        PopulationCalculator.Breakdown result =
                PopulationCalculator.calculate(1_000_000, 1_000_000);

        assertEquals(0, result.nonUrbanPopulation());
        assertEquals(100.0, result.urbanPercentage());
        assertEquals(0.0, result.nonUrbanPercentage());
    }
}
