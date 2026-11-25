package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CityPopulationReportIntegrationTest {

    @Test
    void shouldDisplayCorrectRangoonCityPopulation() throws Exception {

        List<String> expectedRecords = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT Name, CountryCode, District, Population " +
                             "FROM city WHERE Name = 'Rangoon (Yangon)' " +
                             "ORDER BY Name, CountryCode")) {

            while (resultSet.next()) {
                expectedRecords.add(
                        "City: " + resultSet.getString("Name") + System.lineSeparator() +
                                "Country code: " + resultSet.getString("CountryCode") + System.lineSeparator() +
                                "District: " + resultSet.getString("District") + System.lineSeparator() +
                                String.format(
                                        "Population: %,d",
                                        resultSet.getLong("Population")
                                )
                );
            }
        }

        assertFalse(expectedRecords.isEmpty(),
                "Rangoon (Yangon) should exist in the city table");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;

        try (PrintStream capturedOut =
                     new PrintStream(output, true, StandardCharsets.UTF_8)) {

            System.setOut(capturedOut);
            CityPopulationReport.showPopulation("Rangoon (Yangon)");

        } finally {
            System.setOut(originalOut);
        }

        String reportOutput = output.toString(StandardCharsets.UTF_8);

        assertTrue(reportOutput.contains("--- City Population Report ---"));

        for (String expectedRecord : expectedRecords) {
            assertTrue(
                    reportOutput.contains(expectedRecord),
                    "Report should contain the expected city record:\n" + expectedRecord
            );
        }
    }
}