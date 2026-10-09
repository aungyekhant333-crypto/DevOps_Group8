package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CityReport
{
    // Report 9: All cities in the world by population.
    public static void allCitiesInWorld() throws SQLException
    {
        String sql = """
                SELECT city.Name AS City,
                       country.Name AS Country,
                       city.District,
                       city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                ORDER BY city.Population DESC, city.Name ASC
                """;

        executeCityReport(sql);
    }

    // Report 10: All cities in a continent by population.
    public static void allCitiesInContinent(String continent)
            throws SQLException
    {
        String sql = """
                SELECT city.Name AS City,
                       country.Name AS Country,
                       city.District,
                       city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                WHERE country.Continent = ?
                ORDER BY city.Population DESC, city.Name ASC
                """;

        executeCityReport(sql, continent);
    }

    // Report 11: All cities in a region by population.
    public static void allCitiesInRegion(String region)
            throws SQLException
    {
        String sql = """
                SELECT city.Name AS City,
                       country.Name AS Country,
                       city.District,
                       city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                WHERE country.Region = ?
                ORDER BY city.Population DESC, city.Name ASC
                """;

        executeCityReport(sql, region);
    }

    // Report 12: All cities in a country by population.
    public static void allCitiesInCountry(String countryName)
            throws SQLException
    {
        String sql = """
                SELECT city.Name AS City,
                       country.Name AS Country,
                       city.District,
                       city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                WHERE country.Name = ?
                ORDER BY city.Population DESC, city.Name ASC
                """;

        executeCityReport(sql, countryName);
    }

    // Report 13: All cities in a district by population.
    public static void allCitiesInDistrict(String district)
            throws SQLException
    {
        String sql = """
                SELECT city.Name AS City,
                       country.Name AS Country,
                       city.District,
                       city.Population
                FROM city
                JOIN country ON city.CountryCode = country.Code
                WHERE city.District = ?
                ORDER BY city.Population DESC, city.Name ASC
                """;

        executeCityReport(sql, district);
    }

    private static void executeCityReport(String sql, String... parameters)
            throws SQLException
    {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            for (int i = 0; i < parameters.length; i++)
            {
                statement.setString(i + 1, parameters[i]);
            }

            try (ResultSet resultSet = statement.executeQuery())
            {
                System.out.printf("%-35s %-35s %-25s %15s%n",
                        "City", "Country", "District", "Population");

                System.out.println("-".repeat(115));
                while (resultSet.next())
                {
                    System.out.printf("%-35s %-35s %-25s %,15d%n",
                            resultSet.getString("City"),
                            resultSet.getString("Country"),
                            resultSet.getString("District"),
                            resultSet.getInt("Population"));
                }
            }
        }
    }
}