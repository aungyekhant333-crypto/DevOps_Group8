package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PopulationReport {

    /**
     * US04:
     * Returns the total, urban and non-urban population for each region.
     *
     * @return list of regional population reports
     */
    public List<RegionPopulation> getPopulationByRegion() {

        List<RegionPopulation> reports = new ArrayList<>();

        String query = """
        SELECT
            country_totals.Region,
            country_totals.TotalPopulation,
            COALESCE(city_totals.UrbanPopulation, 0) AS UrbanPopulation
        FROM (
            SELECT
                Region,
                SUM(Population) AS TotalPopulation
            FROM country
            GROUP BY Region
        ) country_totals
        LEFT JOIN (
            SELECT
                co.Region,
                SUM(ci.Population) AS UrbanPopulation
            FROM country co
            JOIN city ci
                ON co.Code = ci.CountryCode
            GROUP BY co.Region
        ) city_totals
            ON country_totals.Region = city_totals.Region
        ORDER BY country_totals.Region
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                String region = resultSet.getString("Region");
                long totalPopulation =
                        resultSet.getLong("TotalPopulation");
                long urbanPopulation =
                        resultSet.getLong("UrbanPopulation");

                long nonUrbanPopulation =
                        totalPopulation - urbanPopulation;

                double urbanPercentage =
                        totalPopulation > 0
                                ? (urbanPopulation * 100.0) / totalPopulation
                                : 0;

                double nonUrbanPercentage =
                        totalPopulation > 0
                                ? (nonUrbanPopulation * 100.0) / totalPopulation
                                : 0;

                RegionPopulation report = new RegionPopulation(
                        region,
                        totalPopulation,
                        urbanPopulation,
                        nonUrbanPopulation,
                        urbanPercentage,
                        nonUrbanPercentage
                );

                reports.add(report);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return reports;
    }
}