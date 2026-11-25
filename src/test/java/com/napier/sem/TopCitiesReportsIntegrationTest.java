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

class TopCitiesReportsIntegrationTest {

    private static final int LIMIT = 5;

    @Test
    void shouldReturnTopCitiesWorldwide() throws Exception {
        String sql = """
                SELECT city.Name AS City,
                       country.Name AS Country,
                       city.District,
                       city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                ORDER BY city.Population DESC, city.Name ASC
                LIMIT ?
                """;

        assertReportMatchesDatabase(
                () -> TopCitiesWorldReport.showReport(LIMIT),
                sql, null, LIMIT,
                "--- Top " + LIMIT + " Cities Worldwide ---"
        );
    }

    @Test
    void shouldReturnTopCitiesInAsia() throws Exception {
        String sql = """
                SELECT city.Name AS City,
                       country.Name AS Country,
                       COALESCE(city.District, '') AS District,
                       city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                WHERE country.Continent = ?
                ORDER BY city.Population DESC, city.Name ASC
                LIMIT ?
                """;

        assertReportMatchesDatabase(
                () -> TopCitiesContinentReport.showReport("Asia", LIMIT),
                sql, "Asia", LIMIT,
                "--- Top " + LIMIT + " Cities in Asia ---"
        );
    }

    @Test
    void shouldReturnTopCitiesInMyanmar() throws Exception {
        String sql = """
                SELECT city.Name AS City,
                       country.Name AS Country,
                       COALESCE(city.District, '') AS District,
                       city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                WHERE country.Name = ?
                ORDER BY city.Population DESC, city.Name ASC
                LIMIT ?
                """;

        assertReportMatchesDatabase(
                () -> TopCitiesCountryReport.showReport("Myanmar", LIMIT),
                sql, "Myanmar", LIMIT,
                "--- Top " + LIMIT + " Cities in Myanmar ---"
        );
    }

    @Test
    void shouldReturnTopCitiesInSoutheastAsia() throws Exception {
        String sql = """
                SELECT city.Name AS City,
                       country.Name AS Country,
                       COALESCE(city.District, '') AS District,
                       city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                WHERE country.Region = ?
                ORDER BY city.Population DESC, city.Name ASC
                LIMIT ?
                """;

        assertReportMatchesDatabase(
                () -> TopCitiesRegionReport.showReport(
                        "Southeast Asia", LIMIT
                ),
                sql, "Southeast Asia", LIMIT,
                "--- Top " + LIMIT + " Cities in Southeast Asia ---"
        );
    }

    @Test
    void shouldReturnTopCitiesInRangoonDistrict() throws Exception {
        String sql = """
                SELECT city.Name AS City,
                       country.Name AS Country,
                       COALESCE(city.District, '') AS District,
                       city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                WHERE city.District = ?
                ORDER BY city.Population DESC, city.Name ASC
                LIMIT ?
                """;
        assertReportMatchesDatabase(
                () -> TopCitiesDistrictReport.showReport(
                        "Rangoon (Yangon)", LIMIT
                ),
                sql, "Rangoon (Yangon)", LIMIT,
                "--- Top " + LIMIT + " Cities in Rangoon (Yangon) ---"
        );
    }

    @Test
    void shouldRejectInvalidWorldReportLimit() {
        String output = captureOutput(
                () -> TopCitiesWorldReport.showReport(0)
        );

        assertTrue(output.contains(
                "Please enter a number greater than zero."
        ));
    }

    @Test
    void shouldRejectInvalidContinentInputs() {
        assertTrue(captureOutput(
                () -> TopCitiesContinentReport.showReport(" ", LIMIT)
        ).contains("Please enter a continent name."));

        assertTrue(captureOutput(
                () -> TopCitiesContinentReport.showReport(null, LIMIT)
        ).contains("Please enter a continent name."));

        assertTrue(captureOutput(
                () -> TopCitiesContinentReport.showReport("Asia", 0)
        ).contains("Please enter a number greater than zero."));
    }

    @Test
    void shouldRejectInvalidCountryInputs() {
        assertTrue(captureOutput(
                () -> TopCitiesCountryReport.showReport(" ", LIMIT)
        ).contains("Please enter a country name."));

        assertTrue(captureOutput(
                () -> TopCitiesCountryReport.showReport(null, LIMIT)
        ).contains("Please enter a country name."));

        assertTrue(captureOutput(
                () -> TopCitiesCountryReport.showReport("Myanmar", 0)
        ).contains("Please enter a number greater than zero."));
    }

    @Test
    void shouldRejectInvalidRegionInputs() {
        assertTrue(captureOutput(
                () -> TopCitiesRegionReport.showReport(" ", LIMIT)
        ).contains("Please enter a region name."));

        assertTrue(captureOutput(
                () -> TopCitiesRegionReport.showReport(null, LIMIT)
        ).contains("Please enter a region name."));

        assertTrue(captureOutput(
                () -> TopCitiesRegionReport.showReport(
                        "Southeast Asia", 0
                )
        ).contains("Please enter a number greater than zero."));
    }

    @Test
    void shouldRejectInvalidDistrictInputs() {
        assertTrue(captureOutput(
                () -> TopCitiesDistrictReport.showReport(" ", LIMIT)
        ).contains("Please enter a district name."));

        assertTrue(captureOutput(
                () -> TopCitiesDistrictReport.showReport(null, LIMIT)
        ).contains("Please enter a district name."));

        assertTrue(captureOutput(
                () -> TopCitiesDistrictReport.showReport(
                        "Rangoon (Yangon)", 0
                )
        ).contains("Please enter a number greater than zero."));
    }

    private void assertReportMatchesDatabase(
            Runnable report,
            String sql,
            String filter,
            int limit,
            String expectedTitle
    ) throws Exception {

        List<String> expectedRows = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            if (filter == null) {
                statement.setInt(1, limit);
            } else {
                statement.setString(1, filter);
                statement.setInt(2, limit);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                int rank = 1;
                while (resultSet.next()) {
                    expectedRows.add(String.format(
                            "%-5d %-35s %-35s %-25s %15s",
                            rank++,
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

        String output = captureOutput(report);

        assertTrue(output.contains(expectedTitle));
        assertTrue(output.contains("Rank"));
        assertTrue(output.contains("City"));
        assertTrue(output.contains("Country"));
        assertTrue(output.contains("District"));
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

        long actualRankedRows = output.lines()
                .filter(line -> line.matches("^\\d+\\s+.*"))
                .count();

        assertEquals(
                expectedRows.size(),
                actualRankedRows,
                "The report returned an unexpected number of cities."
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