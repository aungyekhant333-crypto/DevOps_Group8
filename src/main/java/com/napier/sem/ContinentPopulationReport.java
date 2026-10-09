package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContinentPopulationReport {

    public static void showPopulation(String continentName) {
        String sql = """
                SELECT Continent, SUM(Population) AS TotalPopulation
                FROM country
                WHERE Continent = ?
                GROUP BY Continent
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, continentName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("\n--- Continent Population Report ---");
                    System.out.println("Continent: " + resultSet.getString("Continent"));
                    System.out.printf(
                            "Total population: %,d%n",
                            resultSet.getLong("TotalPopulation")
                    );
                } else {
                    System.out.println("Continent not found: " + continentName);
                    System.out.println(
                            "Please check the name and try again."
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Could not retrieve the continent population.");
            System.err.println("Database error: " + e.getMessage());
        }
    }
}