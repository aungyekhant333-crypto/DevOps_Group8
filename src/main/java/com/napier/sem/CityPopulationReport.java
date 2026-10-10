package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CityPopulationReport {

    public static void showPopulation(String cityName) {
        String sql = """
                SELECT Name, CountryCode, District, Population
                FROM city
                WHERE Name = ?
                ORDER BY Name, CountryCode
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cityName);

            try (ResultSet resultSet = statement.executeQuery()) {
                boolean found = false;

                while (resultSet.next()) {
                    if (!found) {
                        System.out.println("\n--- City Population Report ---");
                        found = true;
                    }

                    System.out.println("City: " + resultSet.getString("Name"));
                    System.out.println("Country code: " + resultSet.getString("CountryCode"));
                    System.out.println("District: " + resultSet.getString("District"));
                    System.out.printf(
                            "Population: %,d%n%n",
                            resultSet.getLong("Population")
                    );
                }

                if (!found) {
                    System.out.println("City not found: " + cityName);
                    System.out.println("Please check the name and try again.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Could not retrieve the city population.");
            System.err.println("Database error: " + e.getMessage());
        }
    }
}