package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegionPopulationReport {

    public static void showPopulation(String regionName) {
        String sql = """
                SELECT Region, SUM(Population) AS TotalPopulation
                FROM country
                WHERE Region = ?
                GROUP BY Region
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, regionName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("\n--- Region Population Report ---");
                    System.out.println("Region: " + resultSet.getString("Region"));
                    System.out.printf(
                            "Total population: %,d%n",
                            resultSet.getLong("TotalPopulation")
                    );
                } else {
                    System.out.println("Region not found: " + regionName);
                    System.out.println(
                            "Please check the name and try again."
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Could not retrieve the region population.");
            System.err.println("Database error: " + e.getMessage());
        }
    }
}