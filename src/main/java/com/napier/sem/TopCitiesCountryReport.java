package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TopCitiesCountryReport {

    public static void showReport(String country, int limit) {
        if (country == null || country.isBlank()) {
            System.out.println("Please enter a country name.");
            return;
        }

        if (limit <= 0) {
            System.out.println("Please enter a number greater than zero.");
            return;
        }

        String sql = """
                SELECT city.Name AS City,
                       country.Name AS Country,
                       COALESCE(city.District, '') AS District,
                       city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                WHERE country.Name = ?
                ORDER BY city.Population DESC, city.Name ASC
                LIMIT ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, country);
            statement.setInt(2, limit);

            try (ResultSet resultSet = statement.executeQuery()) {
                System.out.println(
                        "\n--- Top " + limit + " Cities in " + country + " ---"
                );
                System.out.printf("%-5s %-35s %-35s %-25s %15s%n",
                        "Rank", "City", "Country", "District", "Population");
                System.out.println("-".repeat(120));

                int rank = 1;
                boolean found = false;

                while (resultSet.next()) {
                    found = true;

                    System.out.printf(
                            "%-5d %-35s %-35s %-25s %,15d%n",
                            rank++,
                            resultSet.getString("City"),
                            resultSet.getString("Country"),
                            resultSet.getString("District"),
                            resultSet.getLong("Population")
                    );
                }

                if (!found) {
                    System.out.println(
                            "No cities found for country: " + country
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Could not retrieve the top cities in " + country + "."
            );
            System.err.println("Database error: " + e.getMessage());
        }
    }
}