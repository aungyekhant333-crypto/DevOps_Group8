package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TopCountriesRegionReport {

    public static void showReport(String region, int limit) {
        if (region == null || region.isBlank()) {
            System.out.println("Please enter a region name.");
            return;
        }

        if (limit <= 0) {
            System.out.println("Please enter a number greater than zero.");
            return;
        }

        String sql = """
                SELECT Name, Region, Population
                FROM country
                WHERE Region = ?
                ORDER BY Population DESC, Name ASC
                LIMIT ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, region);
            statement.setInt(2, limit);

            try (ResultSet resultSet = statement.executeQuery()) {
                System.out.println(
                        "\n--- Top " + limit + " Countries in " + region + " ---"
                );
                System.out.printf("%-5s %-40s %15s%n",
                        "Rank", "Country", "Population");
                System.out.println("-".repeat(65));

                int rank = 1;
                boolean found = false;

                while (resultSet.next()) {
                    found = true;
                    System.out.printf(
                            "%-5d %-40s %,15d%n",
                            rank++,
                            resultSet.getString("Name"),
                            resultSet.getLong("Population")
                    );
                }

                if (!found) {
                    System.out.println(
                            "No countries found for region: " + region
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Could not retrieve the top countries in the region."
            );
            System.err.println("Database error: " + e.getMessage());
        }
    }
}