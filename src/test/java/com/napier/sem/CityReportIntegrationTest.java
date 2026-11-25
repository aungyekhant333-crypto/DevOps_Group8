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

import static org.junit.jupiter.api.Assertions.*;

class CityReportIntegrationTest {

    @Test
    void shouldReturnAllCitiesWorldwide() throws Exception {
        String sql = """
                SELECT city.Name AS City,
                       country.Name AS Country,
                       city.District,
                       city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                ORDER BY city.Population DESC, city.Name ASC
                """;

        assertReportMatchesDatabase(
                () -> CityReport.allCitiesInWorld(),
                sql, null
        );
    }

    @Test
    void shouldReturnAllCitiesInAsia() throws Exception {
        String sql = """
                SELECT city.Name AS City,
                       country.Name AS Country,
                       city.District,
                       city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                WHERE country.Continent = ?
                ORDER BY city.Population DESC, city.Name ASC
                """;

        assertReportMatchesDatabase(
                () -> CityReport.allCitiesInContinent("Asia"),
                sql, "Asia"
        );
    }

    @Test
    void shouldReturnAllCitiesInSoutheastAsia() throws Exception {
        String sql = """
                SELECT city.Name AS City,
                       country.Name AS Country,
                       city.District,
                       city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                WHERE country.Region = ?
                ORDER BY city.Population DESC, city.Name ASC
                """;

        assertReportMatchesDatabase(
                () -> CityReport.allCitiesInRegion("Southeast Asia"),
                sql, "Southeast Asia"
        );
    }

    @Test
    void shouldReturnAllCitiesInMyanmar() throws Exception {
        String sql = """
                SELECT city.Name AS City,
                       country.Name AS Country,
                       city.District,
                       city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                WHERE country.Name = ?
                ORDER BY city.Population DESC, city.Name ASC
                """;

        assertReportMatchesDatabase(
                () -> CityReport.allCitiesInCountry("Myanmar"),
                sql, "Myanmar"
        );
    }

    @Test
    void shouldReturnAllCitiesInRangoonDistrict() throws Exception {
        String sql = """
                SELECT city.Name AS City,
                       country.Name AS Country,
                       city.District,
                       city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                WHERE city.District = ?
                ORDER BY city.Population DESC, city.Name ASC
                """;

        assertReportMatchesDatabase(
                () -> CityReport.allCitiesInDistrict("Rangoon [Yangon]"),
                sql, "Rangoon [Yangon]"
        );
    }

    @FunctionalInterface
    private interface SqlReport {
        void execute() throws SQLException;
    }

    private void assertReportMatchesDatabase(
            SqlReport report,
            String sql,
            String filter
    ) throws Exception {

        List<String> expectedRows = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {
            if (filter != null) {
                statement.setString(1, filter);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    expectedRows.add(String.format(
                            "%-35s %-35s %-25s %15s",
                            resultSet.getString("City"),
                            resultSet.getString("Country"),
                            resultSet.getString("District"),
                            String.format(
                                    "%,d",
                                    resultSet.getLong("Population")
                            )
                    ));
                }
            }
        }

        assertFalse(
                expectedRows.isEmpty(),
                "The database query should return matching cities."
        );

        String output = captureOutput(report);

        assertTrue(output.contains("City"));
        assertTrue(output.contains("Country"));
        assertTrue(output.contains("District"));
        assertTrue(output.contains("Population"));

        int previousPosition = -1;

        for (String expectedRow : expectedRows) {
            int currentPosition = output.indexOf(expectedRow);

            assertTrue(
                    currentPosition >= 0,
                    "Expected city record was missing:\n" + expectedRow
            );

            assertTrue(
                    currentPosition > previousPosition,
                    "City records are not in the expected population order."
            );

            previousPosition = currentPosition;
        }

        long actualRows = output.lines()
                .filter(line -> line.matches(
                        ".*\\s[\\d,]+$"
                ))
                .count();

        assertEquals(
                expectedRows.size(),
                actualRows,
                "The report returned an unexpected number of cities."
        );
    }

    private String captureOutput(SqlReport report) throws SQLException {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (PrintStream capture = new PrintStream(
                output, true, StandardCharsets.UTF_8
        )) {
            System.setOut(capture);
            report.execute();
        } finally {
            System.setOut(originalOut);
        }

        return output.toString(StandardCharsets.UTF_8);
    }
}
