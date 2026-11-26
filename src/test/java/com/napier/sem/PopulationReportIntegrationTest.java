
package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Integration tests for PopulationReport.
 *
 * These tests verify that regional population reports return the correct
 * total, urban and non-urban population figures and percentages from the
 * World database. They also check that regions are returned in alphabetical
 * order.
 */

class PopulationReportIntegrationTest {

    @Test
    void shouldReturnCorrectPopulationBreakdownForEachRegion()
            throws Exception {

        List<RegionPopulation> reports =
                new PopulationReport().getPopulationByRegion();

        List<RegionPopulation> expectedReports = new ArrayList<>();

        // Independently calculate expected population totals from the database.
        String sql = """
                SELECT
                    country_totals.Region,
                    country_totals.TotalPopulation,
                    COALESCE(city_totals.UrbanPopulation, 0)
                        AS UrbanPopulation
                FROM (
                    SELECT Region, SUM(Population) AS TotalPopulation
                    FROM country
                    GROUP BY Region
                ) country_totals
                LEFT JOIN (
                    SELECT co.Region,
                           SUM(ci.Population) AS UrbanPopulation
                    FROM country co
                    JOIN city ci ON co.Code = ci.CountryCode
                    GROUP BY co.Region
                ) city_totals
                    ON country_totals.Region = city_totals.Region
                ORDER BY country_totals.Region
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                long total = resultSet.getLong("TotalPopulation");
                long urban = resultSet.getLong("UrbanPopulation");

                PopulationCalculator.Breakdown breakdown =
                        PopulationCalculator.calculate(total, urban);

                expectedReports.add(new RegionPopulation(
                        resultSet.getString("Region"),
                        breakdown.totalPopulation(),
                        breakdown.urbanPopulation(),
                        breakdown.nonUrbanPopulation(),
                        breakdown.urbanPercentage(),
                        breakdown.nonUrbanPercentage()
                ));
            }
        }

        assertFalse(expectedReports.isEmpty(),
                "Expected the world database to contain regions.");

        assertEquals(expectedReports.size(), reports.size());

        for (int i = 0; i < expectedReports.size(); i++) {
            RegionPopulation expected = expectedReports.get(i);
            RegionPopulation actual = reports.get(i);

            assertEquals(expected.getRegion(), actual.getRegion());
            assertEquals(expected.getTotalPopulation(),
                    actual.getTotalPopulation());
            assertEquals(expected.getUrbanPopulation(),
                    actual.getUrbanPopulation());
            assertEquals(expected.getNonUrbanPopulation(),
                    actual.getNonUrbanPopulation());
            assertEquals(expected.getUrbanPercentage(),
                    actual.getUrbanPercentage(), 0.0001);
            assertEquals(expected.getNonUrbanPercentage(),
                    actual.getNonUrbanPercentage(), 0.0001);
        }
    }


    @Test
    void shouldReturnRegionsInAlphabeticalOrder() throws Exception {

        List<RegionPopulation> reports =
                new PopulationReport().getPopulationByRegion();

        List<String> expectedRegions = new ArrayList<>();

        // Retrieve the expected region order using MariaDB's sorting rules.
        String sql = """
            SELECT DISTINCT Region
            FROM country
            ORDER BY Region
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                expectedRegions.add(resultSet.getString("Region"));
            }
        }

        List<String> actualRegions = reports.stream()
                .map(RegionPopulation::getRegion)
                .toList();

        assertEquals(expectedRegions, actualRegions,
                "Regions should follow the database's alphabetical order.");
    }
}