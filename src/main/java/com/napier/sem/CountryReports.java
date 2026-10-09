package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CountryReports {

    /**
     * US01:
     * Returns all countries in the world ordered by population,
     * from largest to smallest.
     *
     * @return list of countries ordered by population descending
     */
    public List<Country> getCountriesByPopulation() {

        List<Country> countries = new ArrayList<>();

        String query = """
                SELECT Code, Name, Continent, Region, Population, Capital
                FROM country
                ORDER BY Population DESC
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                Country country = new Country(
                        resultSet.getString("Code"),
                        resultSet.getString("Name"),
                        resultSet.getString("Continent"),
                        resultSet.getString("Region"),
                        resultSet.getInt("Population"),
                        resultSet.getInt("Capital")
                );

                countries.add(country);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return countries;
    }

    /**
     * US02:
     * Returns all countries in a specified continent ordered by population,
     * from largest to smallest.
     *
     * @param continent the continent to filter by
     * @return list of countries in the specified continent
     */
    public List<Country> getCountriesByContinent(String continent) {

        List<Country> countries = new ArrayList<>();

        String query = """
            SELECT Code, Name, Continent, Region, Population, Capital
            FROM country
            WHERE Continent = ?
            ORDER BY Population DESC
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, continent);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Country country = new Country(
                            resultSet.getString("Code"),
                            resultSet.getString("Name"),
                            resultSet.getString("Continent"),
                            resultSet.getString("Region"),
                            resultSet.getInt("Population"),
                            resultSet.getInt("Capital")
                    );

                    countries.add(country);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return countries;
    }

    /**
     * US03:
     * Returns all countries in a specified region ordered by population,
     * from largest to smallest.
     *
     * @param region the region to filter by
     * @return list of countries in the specified region
     */
    public List<Country> getCountriesByRegion(String region) {

        List<Country> countries = new ArrayList<>();

        String query = """
            SELECT Code, Name, Continent, Region, Population, Capital
            FROM country
            WHERE Region = ?
            ORDER BY Population DESC
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, region);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Country country = new Country(
                            resultSet.getString("Code"),
                            resultSet.getString("Name"),
                            resultSet.getString("Continent"),
                            resultSet.getString("Region"),
                            resultSet.getInt("Population"),
                            resultSet.getInt("Capital")
                    );

                    countries.add(country);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return countries;
    }
}