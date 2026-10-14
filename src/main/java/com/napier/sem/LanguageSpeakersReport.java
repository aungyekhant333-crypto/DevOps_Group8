package com.napier.sem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LanguageSpeakersReport {

    public static void showReport() {

        String sql = """
                SELECT
                    cl.Language,
                    SUM(c.Population * cl.Percentage / 100) AS Speakers,
                    (
                        SUM(c.Population * cl.Percentage / 100)
                        / (SELECT SUM(Population) FROM country)
                    ) * 100 AS WorldPercentage
                FROM countrylanguage cl
                JOIN country c ON c.Code = cl.CountryCode
                WHERE cl.Language IN (?, ?, ?, ?, ?)
                GROUP BY cl.Language
                ORDER BY Speakers DESC, cl.Language ASC
                """;

        String[] languages = {
                "Chinese",
                "English",
                "Hindi",
                "Spanish",
                "Arabic"
        };

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (int i = 0; i < languages.length; i++) {
                statement.setString(i + 1, languages[i]);
            }

            try (ResultSet resultSet = statement.executeQuery()) {

                System.out.println(
                        "\n--- Estimated Speakers of Major Languages ---"
                );

                System.out.printf(
                        "%-5s %-15s %20s %20s%n",
                        "Rank",
                        "Language",
                        "Estimated Speakers",
                        "% of World Population"
                );

                System.out.println("-".repeat(65));

                int rank = 1;

                while (resultSet.next()) {
                    System.out.printf(
                            "%-5d %-15s %,20d %19.2f%%%n",
                            rank++,
                            resultSet.getString("Language"),
                            Math.round(resultSet.getDouble("Speakers")),
                            resultSet.getDouble("WorldPercentage")
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println(
                    "Could not retrieve the language speakers report."
            );
            System.err.println("Database error: " + e.getMessage());
        }
    }
}