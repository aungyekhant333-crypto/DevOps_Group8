package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TopCountriesWorldReport {

    public static void showReport(int limit) {
        if (limit <= 0) {
            System.out.println("Please enter a number greater than zero.");
            return;
        }

        String sql = """
                SELECT Name, Population
                FROM country
                ORDER BY Population DESC, Name ASC
                LIMIT ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, limit);

            try (ResultSet resultSet = statement.executeQuery()) {
                System.out.println(
                        "\n--- Top " + limit + " Countries Worldwide ---"
                );
                System.out.printf("%-5s %-40s %15s%n",
                        "Rank", "Country", "Population");
                System.out.println("-".repeat(65));

                int rank = 1;

                while (resultSet.next()) {
                    System.out.printf(
                            "%-5d %-40s %,15d%n",
                            rank++,
                            resultSet.getString("Name"),
                            resultSet.getLong("Population")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Could not retrieve the top countries worldwide."
            );
            System.err.println("Database error: " + e.getMessage());
        }
    }
}