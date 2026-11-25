package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class RegionPopulationReportIntegrationTest {

    @Test
    void shouldDisplayCorrectSoutheastAsianPopulation() throws Exception {

        long expectedPopulation;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT SUM(Population) AS TotalPopulation " +
                             "FROM country WHERE Region = 'Southeast Asia'")) {

            assertTrue(resultSet.next());
            expectedPopulation = resultSet.getLong("TotalPopulation");
            assertFalse(resultSet.wasNull());
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try (PrintStream capturedOut =
                     new PrintStream(output, true, StandardCharsets.UTF_8)) {

            System.setOut(capturedOut);
            RegionPopulationReport.showPopulation("Southeast Asia");

        } finally {
            System.setOut(originalOut);
        }

        String reportOutput = output.toString(StandardCharsets.UTF_8);

        assertTrue(reportOutput.contains("--- Region Population Report ---"));
        assertTrue(reportOutput.contains("Region: Southeast Asia"));
        assertTrue(reportOutput.contains(
                String.format("%,d", expectedPopulation)
        ));
    }
}