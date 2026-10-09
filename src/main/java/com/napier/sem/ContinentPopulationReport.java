package com.napier.sem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ContinentPopulationReport
{
    public static void populationByContinent() throws SQLException
    {
        String sql = """
        SELECT
            country.Continent,
            SUM(country.Population) AS TotalPopulation,
            COALESCE(SUM(city_totals.UrbanPopulation), 0) AS UrbanPopulation,
            SUM(country.Population)
                - COALESCE(SUM(city_totals.UrbanPopulation), 0) AS NonUrbanPopulation
        FROM country
        LEFT JOIN (
            SELECT CountryCode, SUM(Population) AS UrbanPopulation
            FROM city
            GROUP BY CountryCode
        ) AS city_totals ON country.Code = city_totals.CountryCode
        GROUP BY country.Continent
        ORDER BY TotalPopulation DESC
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql))
        {
            System.out.printf("%-20s %18s %18s %22s%n",
                    "Continent",
                    "Total Population",
                    "Urban Population",
                    "Non-Urban Population");

            System.out.println("-".repeat(82));

            while (resultSet.next())
            {
                System.out.printf("%-20s %,18d %,18d %,22d%n",
                        resultSet.getString("Continent"),
                        resultSet.getLong("TotalPopulation"),
                        resultSet.getLong("UrbanPopulation"),
                        resultSet.getLong("NonUrbanPopulation"));
            }
        }
    }
}