package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Retrieves the top-ranked cities by population within a specified region.
 */
public class TopCitiesRegionReport {

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
                SELECT city.Name AS City,
                       country.Name AS Country,
                       COALESCE(city.District, '') AS District,
                       city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                WHERE country.Region = ?
                ORDER BY city.Population DESC, city.Name ASC
                LIMIT ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, region);
            statement.setInt(2, limit);

            try (ResultSet resultSet = statement.executeQuery()) {
                System.out.println(
                        "\n--- Top " + limit + " Cities in " + region + " ---"
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
                            "No cities found for region: " + region
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Could not retrieve the top cities in " + region + "."
            );
            System.err.println("Database error: " + e.getMessage());
        }
    }
}