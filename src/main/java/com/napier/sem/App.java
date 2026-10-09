
package com.napier.sem;

import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        CountryReports countryReports = new CountryReports();
        PopulationReport populationReport = new PopulationReport();

        try (Scanner scanner = new Scanner(System.in)) {

            boolean running = true;

            while (running) {

                System.out.println("\n====================================");
                System.out.println("       DevOps Group 8");
                System.out.println("          My Reports");
                System.out.println("====================================");
                System.out.println("1. All countries by population");
                System.out.println("2. Countries by continent");
                System.out.println("3. Countries by region");
                System.out.println("4. Population by region");
                System.out.println("0. Exit");
                System.out.print("Choose a report: ");

                String choice = scanner.nextLine().trim();

                switch (choice) {

                    case "1" -> {
                        List<Country> countries =
                                countryReports.getCountriesByPopulation();

                        System.out.println("\nCountries by Population");
                        System.out.println("=======================");

                        for (Country country : countries) {
                            printCountry(country);
                        }

                        if (countries.isEmpty()) {
                            System.out.println("No countries found.");
                        }
                    }

                    case "2" -> {
                        System.out.print("Enter continent name: ");
                        String continent = scanner.nextLine().trim();

                        if (continent.isEmpty()) {
                            System.out.println("Continent name cannot be empty.");
                            break;
                        }

                        List<Country> countries =
                                countryReports.getCountriesByContinent(continent);

                        System.out.println("\nCountries in " + continent);
                        System.out.println("==========================");

                        for (Country country : countries) {
                            printCountry(country);
                        }

                        if (countries.isEmpty()) {
                            System.out.println(
                                    "No countries found for that continent."
                            );
                        }
                    }

                    case "3" -> {
                        System.out.print("Enter region name: ");
                        String region = scanner.nextLine().trim();

                        if (region.isEmpty()) {
                            System.out.println("Region name cannot be empty.");
                            break;
                        }

                        List<Country> countries =
                                countryReports.getCountriesByRegion(region);

                        System.out.println("\nCountries in " + region);
                        System.out.println("=======================");

                        for (Country country : countries) {
                            printCountry(country);
                        }

                        if (countries.isEmpty()) {
                            System.out.println(
                                    "No countries found for that region."
                            );
                        }
                    }

                    case "4" -> {
                        List<RegionPopulation> reports =
                                populationReport.getPopulationByRegion();

                        System.out.println("\nPopulation by Region");
                        System.out.println("====================");

                        for (RegionPopulation report : reports) {
                            System.out.println(
                                    report.getRegion() + " | " +
                                            "Total: " + report.getTotalPopulation() + " | " +
                                            "Urban: " + report.getUrbanPopulation() + " (" +
                                            String.format(
                                                    "%.2f",
                                                    report.getUrbanPercentage()
                                            ) + "%) | " +
                                            "Non-Urban: " +
                                            report.getNonUrbanPopulation() + " (" +
                                            String.format(
                                                    "%.2f",
                                                    report.getNonUrbanPercentage()
                                            ) + "%)"
                            );
                        }

                        if (reports.isEmpty()) {
                            System.out.println("No regional population data found.");
                        }
                    }

                    case "0" -> {
                        System.out.println("Goodbye!");
                        running = false;
                    }

                    default -> System.out.println(
                            "Invalid choice. Enter a number from 0 to 4."
                    );
                }

                if (running) {
                    System.out.println("\nPress Enter to return to the menu...");
                    scanner.nextLine();
                }
            }
        }
    }

    private static void printCountry(Country country) {
        System.out.println(
                country.getName() + " | " +
                        "Continent: " + country.getContinent() + " | " +
                        "Region: " + country.getRegion() + " | " +
                        "Population: " + country.getPopulation()
        );
    }
}