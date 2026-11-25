package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class WorldPopulationReportIntegrationTest {

    @Test
    void shouldDisplayCorrectWorldPopulation() throws Exception {

        long expectedPopulation;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT SUM(Population) AS TotalPopulation FROM country")) {

            assertTrue(resultSet.next());
            expectedPopulation = resultSet.getLong("TotalPopulation");
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try (PrintStream capturedOut =
                     new PrintStream(output, true, StandardCharsets.UTF_8)) {

            System.setOut(capturedOut);
            WorldPopulationReport.showPopulation();

        } finally {
            System.setOut(originalOut);
        }

        String reportOutput = output.toString(StandardCharsets.UTF_8);
        String expectedFormatted =
                String.format("%,d", expectedPopulation);

        assertTrue(reportOutput.contains("--- World Population Report ---"));
        assertTrue(reportOutput.contains(expectedFormatted));
    }
}