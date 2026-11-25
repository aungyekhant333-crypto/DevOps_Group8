package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CountryPopulationBreakdownReportIntegrationTest {

    @Test
    void shouldReportPopulationBreakdownForEveryCountry() throws Exception {

        String sql = """
                SELECT country.Name AS Country,
                       country.Population AS TotalPopulation,
                       COALESCE(city_totals.UrbanPopulation, 0) AS UrbanPopulation
                FROM country
                LEFT JOIN (
                    SELECT CountryCode,
                           SUM(Population) AS UrbanPopulation
                    FROM city
                    GROUP BY CountryCode
                ) AS city_totals
                    ON country.Code = city_totals.CountryCode
                ORDER BY country.Name ASC
                """;

        List<String> expectedRows = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                String country = resultSet.getString("Country");
                long totalPopulation = resultSet.getLong("TotalPopulation");
                long urbanPopulation = resultSet.getLong("UrbanPopulation");

                PopulationCalculator.Breakdown breakdown =
                        PopulationCalculator.calculate(
                                totalPopulation,
                                urbanPopulation
                        );

                expectedRows.add(String.format(
                        "%-35s %,18d %,18d (%6.2f%%) %,18d (%6.2f%%)",
                        country,
                        breakdown.totalPopulation(),
                        breakdown.urbanPopulation(),
                        breakdown.urbanPercentage(),
                        breakdown.nonUrbanPopulation(),
                        breakdown.nonUrbanPercentage()
                ));
            }
        }

        assertTrue(
                !expectedRows.isEmpty(),
                "The database should contain country records."
        );

        String output = captureReportOutput();
        List<String> outputLines = output.lines().toList();

        assertTrue(
                output.contains("--- Country Population Breakdown ---"),
                "The report should display its title."
        );

        assertTrue(
                output.contains("Country")
                        && output.contains("Total Population")
                        && output.contains("Living in Cities")
                        && output.contains("Not Living in Cities"),
                "The report should display all population breakdown headings."
        );

        int separatorIndex = outputLines.indexOf("-".repeat(120));

        assertTrue(
                separatorIndex >= 0,
                "The report should display its separator line."
        );

        List<String> actualRows = outputLines
                .subList(separatorIndex + 1, outputLines.size())
                .stream()
                .filter(line -> !line.isBlank())
                .toList();

        assertEquals(
                expectedRows,
                actualRows,
                "The report should display every country's population breakdown "
                        + "with correct totals, urban populations, non-urban "
                        + "populations, percentages and alphabetical ordering."
        );
    }

    private String captureReportOutput() {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (PrintStream capture = new PrintStream(
                outputStream,
                true,
                StandardCharsets.UTF_8
        )) {
            System.setOut(capture);
            CountryPopulationBreakdownReport.showReport();

        } finally {
            System.setOut(originalOut);
        }

        return outputStream.toString(StandardCharsets.UTF_8);
    }
}
