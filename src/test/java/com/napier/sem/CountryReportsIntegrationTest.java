package com.napier.sem;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CountryReportsIntegrationTest {

    private final CountryReports countryReports = new CountryReports();

    @Test
    void shouldReturnAllCountriesOrderedByPopulation() throws Exception {

        List<Country> expected = getExpectedCountries(
                "SELECT Code, Name, Continent, Region, Population, Capital " +
                        "FROM country ORDER BY Population DESC"
        );

        List<Country> actual = countryReports.getCountriesByPopulation();

        assertEquals(expected.size(), actual.size());
        assertFalse(actual.isEmpty());

        assertCountriesMatch(expected, actual);
        assertPopulationDescending(actual);
    }

    @Test
    void shouldReturnCountriesInAsiaOrderedByPopulation() throws Exception {

        List<Country> expected = getExpectedCountries(
                "SELECT Code, Name, Continent, Region, Population, Capital " +
                        "FROM country WHERE Continent = 'Asia' " +
                        "ORDER BY Population DESC"
        );

        List<Country> actual =
                countryReports.getCountriesByContinent("Asia");

        assertEquals(expected.size(), actual.size());
        assertFalse(actual.isEmpty());

        assertCountriesMatch(expected, actual);
        assertTrue(actual.stream().allMatch(
                country -> "Asia".equals(country.getContinent())
        ));
        assertPopulationDescending(actual);
    }

    @Test
    void shouldReturnCountriesInSoutheastAsiaOrderedByPopulation()
            throws Exception {

        List<Country> expected = getExpectedCountries(
                "SELECT Code, Name, Continent, Region, Population, Capital " +
                        "FROM country WHERE Region = 'Southeast Asia' " +
                        "ORDER BY Population DESC"
        );

        List<Country> actual =
                countryReports.getCountriesByRegion("Southeast Asia");

        assertEquals(expected.size(), actual.size());
        assertFalse(actual.isEmpty());

        assertCountriesMatch(expected, actual);
        assertTrue(actual.stream().allMatch(
                country -> "Southeast Asia".equals(country.getRegion())
        ));
        assertPopulationDescending(actual);
    }

    private List<Country> getExpectedCountries(String sql)
            throws Exception {

        List<Country> countries = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                countries.add(new Country(
                        resultSet.getString("Code"),
                        resultSet.getString("Name"),
                        resultSet.getString("Continent"),
                        resultSet.getString("Region"),
                        resultSet.getInt("Population"),
                        resultSet.getInt("Capital")
                ));
            }
        }

        return countries;
    }

    private void assertCountriesMatch(
            List<Country> expected,
            List<Country> actual) {

        for (int i = 0; i < expected.size(); i++) {

            Country expectedCountry = expected.get(i);
            Country actualCountry = actual.get(i);
            assertEquals(expectedCountry.getCode(), actualCountry.getCode());
            assertEquals(expectedCountry.getName(), actualCountry.getName());
            assertEquals(expectedCountry.getContinent(),
                    actualCountry.getContinent());
            assertEquals(expectedCountry.getRegion(),
                    actualCountry.getRegion());
            assertEquals(expectedCountry.getPopulation(),
                    actualCountry.getPopulation());
            assertEquals(expectedCountry.getCapital(),
                    actualCountry.getCapital());
        }
    }

    private void assertPopulationDescending(List<Country> countries) {

        for (int i = 1; i < countries.size(); i++) {
            assertTrue(
                    countries.get(i - 1).getPopulation()
                            >= countries.get(i).getPopulation(),
                    "Countries should be ordered by descending population"
            );
        }
    }
}