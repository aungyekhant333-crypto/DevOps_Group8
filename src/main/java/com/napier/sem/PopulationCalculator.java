package com.napier.sem;

/**
 * Calculates total, urban and non-urban population statistics.
 */
public final class PopulationCalculator {

    private PopulationCalculator() {
        // Utility class; prevent instantiation.
    }

    /**
     * Holds the results of a population calculation.
     */
    public record Breakdown(
            long totalPopulation,
            long urbanPopulation,
            long nonUrbanPopulation,
            double urbanPercentage,
            double nonUrbanPercentage
    ) {}

    /**
     * Calculates the urban and non-urban population breakdown.
     *
     * @param totalPopulation total population
     * @param urbanPopulation population living in cities
     * @return calculated population breakdown
     */
    public static Breakdown calculate(
            long totalPopulation,
            long urbanPopulation
    ) {
        long nonUrbanPopulation = totalPopulation - urbanPopulation;

        double urbanPercentage = totalPopulation > 0
                ? (urbanPopulation * 100.0) / totalPopulation
                : 0.0;

        double nonUrbanPercentage = totalPopulation > 0
                ? (nonUrbanPopulation * 100.0) / totalPopulation
                : 0.0;

        return new Breakdown(
                totalPopulation,
                urbanPopulation,
                nonUrbanPopulation,
                urbanPercentage,
                nonUrbanPercentage
        );
    }
}
