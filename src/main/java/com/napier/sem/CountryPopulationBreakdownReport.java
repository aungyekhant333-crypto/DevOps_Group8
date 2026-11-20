package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Reports a country's total population and its urban and non-urban population breakdown.
 */
public class CountryPopulationBreakdownReport {

    public static void showReport() {

        String sql = """
                SELECT country.Name AS Country,
                       country.Population AS TotalPopulation,
                       COALESCE(city_totals.UrbanPopulation, 0) AS UrbanPopulation
                FROM country
                LEFT JOIN (
                    SELECT CountryCode,
                           SUM(Population) AS UrbanPopulation
                    FROM city
                    GROUP BY CountryCode
                ) AS city_totals
                    ON country.Code = city_totals.CountryCode
                ORDER BY country.Name ASC
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("\n--- Country Population Breakdown ---");

            System.out.printf(
                    "%-35s %18s %30s %30s%n",
                    "Country",
                    "Total Population",
                    "Living in Cities",
                    "Not Living in Cities"
            );

            System.out.println("-".repeat(120));

            boolean found = false;

            while (resultSet.next()) {
                found = true;

                String country = resultSet.getString("Country");
                long totalPopulation = resultSet.getLong("TotalPopulation");
                long urbanPopulation = resultSet.getLong("UrbanPopulation");
                long nonUrbanPopulation = totalPopulation - urbanPopulation;

                double urbanPercentage = 0;
                double nonUrbanPercentage = 0;

                if (totalPopulation > 0) {
                    urbanPercentage =
                            (urbanPopulation * 100.0) / totalPopulation;

                    nonUrbanPercentage =
                            (nonUrbanPopulation * 100.0) / totalPopulation;
                }

                System.out.printf(
                        "%-35s %,18d %,18d (%6.2f%%) %,18d (%6.2f%%)%n",
                        country,
                        totalPopulation,
                        urbanPopulation,
                        urbanPercentage,
                        nonUrbanPopulation,
                        nonUrbanPercentage
                );
            }

            if (!found) {
                System.out.println("No country records found.");
            }

        } catch (SQLException e) {
            System.err.println(
                    "Could not retrieve the country population breakdown."
            );
            System.err.println("Database error: " + e.getMessage());
        }
    }
}