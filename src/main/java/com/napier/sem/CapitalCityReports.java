package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CapitalCityReports {

    // Report 1: All capital cities in the world.
    public static void showAllWorldCapitals() {
        showReport("All World Capital Cities", null, null, null);
    }

    // Report 2: All capital cities in a continent.
    public static void showAllCapitalsInContinent(String continent) {
        showReport("Capital Cities in " + continent,
                "c.Continent", continent, null);
    }

    // Report 3: All capital cities in a region.
    public static void showAllCapitalsInRegion(String region) {
        showReport("Capital Cities in " + region,
                "c.Region", region, null);
    }

    // Report 4: Top N capital cities in the world.
    public static void showTopWorldCapitals(int n) {
        showReport("Top " + n + " World Capital Cities",
                null, null, n);
    }

    // Report 5: Top N capital cities in a continent.
    public static void showTopCapitalsInContinent(String continent, int n) {
        showReport("Top " + n + " Capital Cities in " + continent,
                "c.Continent", continent, n);
    }

    // Report 6: Top N capital cities in a region.
    public static void showTopCapitalsInRegion(String region, int n) {
        showReport("Top " + n + " Capital Cities in " + region,
                "c.Region", region, n);
    }

    // Shared query and output logic for all six reports.
    private static void showReport(
            String title,
            String filterColumn,
            String filterValue,
            Integer limit) {

        StringBuilder sql = new StringBuilder("""
                SELECT ci.Name AS CapitalCity,
                       c.Name AS Country,
                       c.Continent,
                       c.Region,
                       ci.Population
                FROM country c
                JOIN city ci ON c.Capital = ci.ID
                """);

        if (filterColumn != null) {
            sql.append(" WHERE ").append(filterColumn).append(" = ?");
        }

        sql.append(" ORDER BY ci.Population DESC, ci.Name ASC");

        if (limit != null) {
            sql.append(" LIMIT ?");
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql.toString())) {

            int parameterIndex = 1;

            if (filterColumn != null) {
                statement.setString(parameterIndex++, filterValue);
            }

            if (limit != null) {
                statement.setInt(parameterIndex, limit);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                System.out.println("\n--- " + title + " ---");

                System.out.printf(
                        "%-30s | %-35s | %-20s | %-26s | %15s%n",
                        "Capital City", "Country", "Continent",
                        "Region", "Population");

                System.out.println("-".repeat(137));

                boolean found = false;

                while (resultSet.next()) {
                    found = true;

                    System.out.printf(
                            "%-30s | %-35s | %-20s | %-26s | %,15d%n",
                            resultSet.getString("CapitalCity"),
                            resultSet.getString("Country"),
                            resultSet.getString("Continent"),
                            resultSet.getString("Region"),
                            resultSet.getLong("Population"));
                }

                if (!found) {
                    System.out.println("No matching capital cities found.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Could not retrieve the capital city report.");
            System.err.println("Database error: " + e.getMessage());
        }
    }
}