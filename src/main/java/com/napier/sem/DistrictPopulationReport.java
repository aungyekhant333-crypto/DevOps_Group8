package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DistrictPopulationReport {

    public static void showPopulation(String districtName) {
        String sql = """
                SELECT District, SUM(Population) AS TotalPopulation
                FROM city
                WHERE District = ?
                GROUP BY District
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, districtName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("\n--- District Population Report ---");
                    System.out.println("District: " + resultSet.getString("District"));
                    System.out.printf(
                            "Total population: %,d%n",
                            resultSet.getLong("TotalPopulation")
                    );
                } else {
                    System.out.println("District not found: " + districtName);
                    System.out.println(
                            "Please check the name and try again."
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Could not retrieve the district population.");
            System.err.println("Database error: " + e.getMessage());
        }
    }
}