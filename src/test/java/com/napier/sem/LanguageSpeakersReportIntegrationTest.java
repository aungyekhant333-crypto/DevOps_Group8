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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LanguageSpeakersReportIntegrationTest {

    @Test
    void shouldReportEstimatedSpeakersForMajorLanguages() throws Exception {

        String sql = """
                SELECT
                    cl.Language,
                    SUM(c.Population * cl.Percentage / 100) AS Speakers,
                    (
                        SUM(c.Population * cl.Percentage / 100)
                        / (SELECT SUM(Population) FROM country)
                    ) * 100 AS WorldPercentage
                FROM countrylanguage cl
                JOIN country c ON c.Code = cl.CountryCode
                WHERE cl.Language IN (?, ?, ?, ?, ?)
                GROUP BY cl.Language
                ORDER BY Speakers DESC, cl.Language ASC
                """;

        String[] languages = {
                "Chinese",
                "English",
                "Hindi",
                "Spanish",
                "Arabic"
        };

        List<String> expectedRows = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (int i = 0; i < languages.length; i++) {
                statement.setString(i + 1, languages[i]);
            }

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    expectedRows.add(String.format(
                            "%-5d %-15s %,20d %19.2f%%",
                            expectedRows.size() + 1,
                            resultSet.getString("Language"),
                            Math.round(resultSet.getDouble("Speakers")),
                            resultSet.getDouble("WorldPercentage")
                    ));
                }
            }
        }

        assertEquals(
                5,
                expectedRows.size(),
                "The database should contain records for all five major languages."
        );

        String output = captureReportOutput();
        List<String> outputLines = output.lines().toList();

        assertTrue(
                output.contains(
                        "--- Estimated Speakers of Major Languages ---"
                ),
                "The report should display its title."
        );

        assertTrue(
                output.contains("Rank")
                        && output.contains("Language")
                        && output.contains("Estimated Speakers")
                        && output.contains("% of World Population"),
                "The report should display all expected column headings."
        );

        int separatorIndex = outputLines.indexOf("-".repeat(65));

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
                "The report should display the correct language rankings, "
                        + "estimated speakers and world population percentages."
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
            LanguageSpeakersReport.showReport();

        } finally {
            System.setOut(originalOut);
        }

        return outputStream.toString(StandardCharsets.UTF_8);
    }
}