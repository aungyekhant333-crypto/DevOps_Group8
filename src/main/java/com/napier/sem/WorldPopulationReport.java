package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WorldPopulationReport {

    public static void showPopulation() {
        String sql = "SELECT SUM(Population) AS TotalPopulation FROM country";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                long population = resultSet.getLong("TotalPopulation");

                System.out.println("\n--- World Population Report ---");
                System.out.printf("Total population: %,d%n", population);
            }

        } catch (SQLException e) {
            System.err.println("Could not retrieve the world population.");
            System.err.println("Database error: " + e.getMessage());
        }
    }
}