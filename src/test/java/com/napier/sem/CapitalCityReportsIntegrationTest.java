package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CapitalCityReportsIntegrationTest {

    private static final int LIMIT = 5;

    @Test
    void shouldReturnAllWorldCapitals() throws Exception {
        String sql = """
                SELECT ci.Name AS CapitalCity,
                       c.Name AS Country,
                       c.Continent,
                       c.Region,
                       ci.Population
                FROM country c
                JOIN city ci ON c.Capital = ci.ID
                ORDER BY ci.Population DESC, ci.Name ASC
                """;

        assertReportMatchesDatabase(
                () -> CapitalCityReports.showAllWorldCapitals(),
                sql, null, null,
                "--- All World Capital Cities ---"
        );
    }

    @Test
    void shouldReturnAllCapitalsInAsia() throws Exception {
        String sql = """
                SELECT ci.Name AS CapitalCity,
                       c.Name AS Country,
                       c.Continent,
                       c.Region,
                       ci.Population
                FROM country c
                JOIN city ci ON c.Capital = ci.ID
                WHERE c.Continent = ?
                ORDER BY ci.Population DESC, ci.Name ASC
                """;

        assertReportMatchesDatabase(
                () -> CapitalCityReports.showAllCapitalsInContinent("Asia"),
                sql, "Asia", null,
                "--- Capital Cities in Asia ---"
        );
    }

    @Test
    void shouldReturnAllCapitalsInSoutheastAsia() throws Exception {
        String sql = """
                SELECT ci.Name AS CapitalCity,
                       c.Name AS Country,
                       c.Continent,
                       c.Region,
                       ci.Population
                FROM country c
                JOIN city ci ON c.Capital = ci.ID
                WHERE c.Region = ?
                ORDER BY ci.Population DESC, ci.Name ASC
                """;

        assertReportMatchesDatabase(
                () -> CapitalCityReports.showAllCapitalsInRegion(
                        "Southeast Asia"
                ),
                sql, "Southeast Asia", null,
                "--- Capital Cities in Southeast Asia ---"
        );
    }

    @Test
    void shouldReturnTopWorldCapitals() throws Exception {
        String sql = """
                SELECT ci.Name AS CapitalCity,
                       c.Name AS Country,
                       c.Continent,
                       c.Region,
                       ci.Population
                FROM country c
                JOIN city ci ON c.Capital = ci.ID
                ORDER BY ci.Population DESC, ci.Name ASC
                LIMIT ?
                """;

        assertReportMatchesDatabase(
                () -> CapitalCityReports.showTopWorldCapitals(LIMIT),
                sql, null, LIMIT,
                "--- Top " + LIMIT + " World Capital Cities ---"
        );
    }

    @Test
    void shouldReturnTopCapitalsInAsia() throws Exception {
        String sql = """
                SELECT ci.Name AS CapitalCity,
                       c.Name AS Country,
                       c.Continent,
                       c.Region,
                       ci.Population
                FROM country c
                JOIN city ci ON c.Capital = ci.ID
                WHERE c.Continent = ?
                ORDER BY ci.Population DESC, ci.Name ASC
                LIMIT ?
                """;

        assertReportMatchesDatabase(
                () -> CapitalCityReports.showTopCapitalsInContinent(
                        "Asia", LIMIT
                ),
                sql, "Asia", LIMIT,
                "--- Top " + LIMIT + " Capital Cities in Asia ---"
        );
    }
    @Test
    void shouldReturnTopCapitalsInSoutheastAsia() throws Exception {
        String sql = """
                SELECT ci.Name AS CapitalCity,
                       c.Name AS Country,
                       c.Continent,
                       c.Region,
                       ci.Population
                FROM country c
                JOIN city ci ON c.Capital = ci.ID
                WHERE c.Region = ?
                ORDER BY ci.Population DESC, ci.Name ASC
                LIMIT ?
                """;

        assertReportMatchesDatabase(
                () -> CapitalCityReports.showTopCapitalsInRegion(
                        "Southeast Asia", LIMIT
                ),
                sql, "Southeast Asia", LIMIT,
                "--- Top " + LIMIT
                        + " Capital Cities in Southeast Asia ---"
        );
    }

    private void assertReportMatchesDatabase(
            Runnable report,
            String sql,
            String filter,
            Integer limit,
            String expectedTitle
    ) throws Exception {

        List<String> expectedRows = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            int parameterIndex = 1;

            if (filter != null) {
                statement.setString(parameterIndex++, filter);
            }

            if (limit != null) {
                statement.setInt(parameterIndex, limit);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    expectedRows.add(String.format(
                            "%-30s | %-35s | %-20s | %-26s | %15s",
                            resultSet.getString("CapitalCity"),
                            resultSet.getString("Country"),
                            resultSet.getString("Continent"),
                            resultSet.getString("Region"),
                            String.format(
                                    "%,d",
                                    resultSet.getLong("Population")
                            )
                    ));
                }
            }
        }

        String output = captureOutput(report);

        assertTrue(output.contains(expectedTitle));
        assertTrue(output.contains("Capital City"));
        assertTrue(output.contains("Country"));
        assertTrue(output.contains("Continent"));
        assertTrue(output.contains("Region"));
        assertTrue(output.contains("Population"));

        int previousPosition = -1;

        for (String expectedRow : expectedRows) {
            int currentPosition = output.indexOf(expectedRow);

            assertTrue(
                    currentPosition >= 0,
                    "Expected report row was missing: " + expectedRow
            );

            assertTrue(
                    currentPosition > previousPosition,
                    "Report rows are not in the expected order."
            );

            previousPosition = currentPosition;
        }

        long actualRows = output.lines()
                .filter(line -> line.matches(
                        ".*\\|\\s*[\\d,]+\\s*$"
                ))
                .count();

        assertEquals(
                expectedRows.size(),
                actualRows,
                "The report returned an unexpected number of capital cities."
        );
    }

    private String captureOutput(Runnable report) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (PrintStream capture = new PrintStream(
                output, true, StandardCharsets.UTF_8
        )) {
            System.setOut(capture);
            report.run();
        } finally {
            System.setOut(originalOut);
        }

        return output.toString(StandardCharsets.UTF_8);
    }
}