package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CountryPopulationReport {

    public static void showPopulation(String countryName) {
        String sql = "SELECT Name, Population FROM country WHERE Name = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, countryName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    System.out.println("\n--- Country Population Report ---");
                    System.out.println("Country: " + resultSet.getString("Name"));
                    System.out.printf(
                            "Population: %,d%n",
                            resultSet.getLong("Population")
                    );
                } else {
                    System.out.println(
                            "Country not found: " + countryName
                    );
                    System.out.println(
                            "Please check the spelling and try again."
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Could not retrieve the country population.");
            System.err.println("Database error: " + e.getMessage());
        }
    }
}