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

class TopCountriesReportsIntegrationTest {

    private static final int LIMIT = 5;

    @Test
    void shouldReturnTopCountriesWorldwide() throws Exception {
        String sql = """
                SELECT Name, Population
                FROM country
                ORDER BY Population DESC, Name ASC
                LIMIT ?
                """;

        assertReportMatchesDatabase(
                () -> TopCountriesWorldReport.showReport(LIMIT),
                sql,
                null,
                LIMIT,
                "--- Top " + LIMIT + " Countries Worldwide ---"
        );
    }

    @Test
    void shouldReturnTopCountriesInAsia() throws Exception {
        String sql = """
                SELECT Name, Population
                FROM country
                WHERE Continent = ?
                ORDER BY Population DESC, Name ASC
                LIMIT ?
                """;

        assertReportMatchesDatabase(
                () -> TopCountriesContinentReport.showReport("Asia", LIMIT),
                sql,
                "Asia",
                LIMIT,
                "--- Top " + LIMIT + " Countries in Asia ---"
        );
    }

    @Test
    void shouldReturnTopCountriesInSoutheastAsia() throws Exception {
        String sql = """
                SELECT Name, Population
                FROM country
                WHERE Region = ?
                ORDER BY Population DESC, Name ASC
                LIMIT ?
                """;

        assertReportMatchesDatabase(
                () -> TopCountriesRegionReport.showReport(
                        "Southeast Asia", LIMIT
                ),
                sql,
                "Southeast Asia",
                LIMIT,
                "--- Top " + LIMIT + " Countries in Southeast Asia ---"
        );
    }

    @Test
    void shouldRejectInvalidWorldReportLimit() {
        String output = captureOutput(
                () -> TopCountriesWorldReport.showReport(0)
        );

        assertTrue(output.contains(
                "Please enter a number greater than zero."
        ));
    }

    @Test
    void shouldRejectInvalidContinentReportInputs() {
        String blankOutput = captureOutput(
                () -> TopCountriesContinentReport.showReport(" ", LIMIT)
        );

        assertTrue(blankOutput.contains(
                "Please enter a continent name."
        ));

        String nullOutput = captureOutput(
                () -> TopCountriesContinentReport.showReport(null, LIMIT)
        );

        assertTrue(nullOutput.contains(
                "Please enter a continent name."
        ));

        String invalidLimitOutput = captureOutput(
                () -> TopCountriesContinentReport.showReport("Asia", 0)
        );

        assertTrue(invalidLimitOutput.contains(
                "Please enter a number greater than zero."
        ));
    }

    @Test
    void shouldRejectInvalidRegionReportInputs() {
        String blankOutput = captureOutput(
                () -> TopCountriesRegionReport.showReport(" ", LIMIT)
        );

        assertTrue(blankOutput.contains(
                "Please enter a region name."
        ));

        String nullOutput = captureOutput(
                () -> TopCountriesRegionReport.showReport(null, LIMIT)
        );

        assertTrue(nullOutput.contains(
                "Please enter a region name."
        ));

        String invalidLimitOutput = captureOutput(
                () -> TopCountriesRegionReport.showReport(
                        "Southeast Asia", 0
                )
        );

        assertTrue(invalidLimitOutput.contains(
                "Please enter a number greater than zero."
        ));
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
                            "%-5d %-40s %15s",
                            rank++,
                            resultSet.getString("Name"),
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
        assertTrue(output.contains("Country"));
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
                "The report returned an unexpected number of countries."
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