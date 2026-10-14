
package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ContinentPopulationReport {

    // Report 22: Population lookup for one specified continent
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
                    System.out.println("Please check the name and try again.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Could not retrieve the continent population.");
            System.err.println("Database error: " + e.getMessage());
        }
    }

    // Report 14: Total, urban and non-urban population by continent
    public static void populationByContinent() {
        String sql = """
                SELECT
                    co.Continent,
                    SUM(co.Population) AS TotalPopulation,
                    SUM(COALESCE(ct.UrbanPopulation, 0)) AS UrbanPopulation,
                    SUM(co.Population)
                        - SUM(COALESCE(ct.UrbanPopulation, 0))
                        AS NonUrbanPopulation
                FROM country co
                LEFT JOIN (
                    SELECT CountryCode, SUM(Population) AS UrbanPopulation
                    FROM city
                    GROUP BY CountryCode
                ) ct ON co.Code = ct.CountryCode
                GROUP BY co.Continent
                ORDER BY co.Continent
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("\n--- Population Breakdown by Continent ---");
            System.out.printf(
                    "%-25s %15s %15s %15s %12s %15s%n",
                    "Continent",
                    "Total",
                    "Urban",
                    "Non-urban",
                    "Urban %",
                    "Non-urban %"
            );

            while (resultSet.next()) {
                String continent = resultSet.getString("Continent");
                long total = resultSet.getLong("TotalPopulation");
                long urban = resultSet.getLong("UrbanPopulation");
                long nonUrban = resultSet.getLong("NonUrbanPopulation");

                double urbanPercentage =
                        total > 0 ? urban * 100.0 / total : 0.0;

                double nonUrbanPercentage =
                        total > 0 ? nonUrban * 100.0 / total : 0.0;

                System.out.printf(
                        "%-25s %,15d %,15d %,15d %11.2f%% %14.2f%%%n",
                        continent,
                        total,
                        urban,
                        nonUrban,
                        urbanPercentage,
                        nonUrbanPercentage
                );
            }

        } catch (SQLException e) {
            System.err.println(
                    "Could not retrieve the population breakdown by continent."
            );
            System.err.println("Database error: " + e.getMessage());
        }
    }
}
