package com.napier.sem;

import java.sql.SQLException;
import java.util.Scanner;

public class App
{
    public static void main(String[] args)
    {
        try (Scanner scanner = new Scanner(System.in))
        {
            boolean running = true;

            while (running)
            {
                System.out.println("\n===== DEVOPS GROUP 8: POPULATION REPORTS =====");
                System.out.println("9. All cities in the world by population");
                System.out.println("10. All cities in a continent by population");
                System.out.println("11. All cities in a region by population");
                System.out.println("12. All cities in a country by population");
                System.out.println("13. All cities in a district by population");
                System.out.println("14. Population, urban and non-urban population by continent");
                System.out.println("0. Exit");
                System.out.print("Choose a report: ");

                String choice = scanner.nextLine().trim();

                try
                {
                    switch (choice)
                    {
                        case "9" ->
                        {
                            System.out.println("\nREPORT 9: ALL CITIES IN THE WORLD");
                            CityReport.allCitiesInWorld();
                        }

                        case "10" ->
                        {
                            System.out.print("Enter continent name: ");
                            String continent = scanner.nextLine().trim();
                            System.out.println("\nREPORT 10: CITIES IN " + continent);
                            CityReport.allCitiesInContinent(continent);
                        }

                        case "11" ->
                        {
                            System.out.print("Enter region name: ");
                            String region = scanner.nextLine().trim();
                            System.out.println("\nREPORT 11: CITIES IN " + region);
                            CityReport.allCitiesInRegion(region);
                        }

                        case "12" ->
                        {
                            System.out.print("Enter country name: ");
                            String country = scanner.nextLine().trim();
                            System.out.println("\nREPORT 12: CITIES IN " + country);
                            CityReport.allCitiesInCountry(country);
                        }

                        case "13" ->
                        {
                            System.out.print("Enter district name: ");
                            String district = scanner.nextLine().trim();
                            System.out.println("\nREPORT 13: CITIES IN " + district);
                            CityReport.allCitiesInDistrict(district);
                        }

                        case "14" ->
                        {
                            System.out.println("\nREPORT 14: POPULATION BY CONTINENT");
                            ContinentPopulationReport.populationByContinent();
                        }

                        case "0" ->
                        {
                            running = false;
                            System.out.println("Exiting population reports. Goodbye!");
                        }

                        default ->
                                System.out.println("Invalid choice. Please enter 9–14 or 0.");
                    }
                }
                catch (SQLException e)
                {
                    System.err.println("Unable to run the report: " + e.getMessage());
                }
            }
        }
    }
}