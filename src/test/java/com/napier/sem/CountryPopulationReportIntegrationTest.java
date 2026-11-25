package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class CountryPopulationReportIntegrationTest {

    @Test
    void shouldDisplayCorrectMyanmarPopulation() throws Exception {

        long expectedPopulation;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT Population FROM country WHERE Name = 'Myanmar'")) {

            assertTrue(resultSet.next(), "Myanmar should exist in the country table");
            expectedPopulation = resultSet.getLong("Population");
            assertFalse(resultSet.wasNull());
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try (PrintStream capturedOut =
                     new PrintStream(output, true, StandardCharsets.UTF_8)) {

            System.setOut(capturedOut);
            CountryPopulationReport.showPopulation("Myanmar");

        } finally {
            System.setOut(originalOut);
        }

        String reportOutput = output.toString(StandardCharsets.UTF_8);

        assertTrue(reportOutput.contains("--- Country Population Report ---"));
        assertTrue(reportOutput.contains("Country: Myanmar"));
        assertTrue(reportOutput.contains(
                String.format("%,d", expectedPopulation)
        ));
    }
}