package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class DistrictPopulationReportIntegrationTest {

    @Test
    void shouldDisplayCorrectYangonDistrictPopulation() throws Exception {

        long expectedPopulation;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT SUM(Population) AS TotalPopulation " +
                             "FROM city WHERE District = 'Rangoon [Yangon]'")) {

            assertTrue(resultSet.next(),
                    "The Yangon district query should return a result");

            expectedPopulation = resultSet.getLong("TotalPopulation");
            assertFalse(resultSet.wasNull());
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try (PrintStream capturedOut =
                     new PrintStream(output, true, StandardCharsets.UTF_8)) {

            System.setOut(capturedOut);
            DistrictPopulationReport.showPopulation("Rangoon [Yangon]");

        } finally {
            System.setOut(originalOut);
        }

        String reportOutput = output.toString(StandardCharsets.UTF_8);

        assertTrue(reportOutput.contains("--- District Population Report ---"));
        assertTrue(reportOutput.contains("District: Rangoon [Yangon]"));
        assertTrue(reportOutput.contains(
                String.format("%,d", expectedPopulation)
        ));
    }
}