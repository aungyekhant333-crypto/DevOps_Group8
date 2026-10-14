
package com.napier.sem;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        CountryReports countryReports = new CountryReports();
        PopulationReport populationReport = new PopulationReport();

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;

            while (running) {
                printMainMenu();
                System.out.print("Choose a category (0 to exit): ");
                String choice = scanner.nextLine().trim();

                try {
                    switch (choice) {
                        case "1" ->
                                showCountriesMenu(scanner, countryReports,
                                        populationReport);
                        case "2" -> showCityMenu(scanner);
                        case "3" -> showCapitalMenu(scanner);
                        case "4" -> showPopulationMenu(scanner);
                        case "5" -> showTopCitiesMenu(scanner);
                        case "0" -> {
                            running = false;
                            System.out.println("Goodbye!");
                        }
                        default ->
                                System.out.println("Invalid choice. Select 0–5.");
                    }
                } catch (SQLException e) {
                    System.err.println("The report could not be completed.");
                    System.err.println("Database error: " + e.getMessage());
                }

                if (running) {
                    System.out.println("\nPress Enter to return to the main menu...");
                    scanner.nextLine();
                }
            }
        }
    }

    private static void printMainMenu() {
        System.out.println("\n========== DEVOPS GROUP 8: WORLD REPORTS ==========");
        System.out.println("1. Countries Reports (Mya and Aung)");
        System.out.println("2. City Reports (Rosy)");
        System.out.println("3. Capital Reports (Oscar)");
        System.out.println("4. Population Reports (Khine)");
        System.out.println("5. Cities Reports (Yoon)");
        System.out.println("0. Exit");
        System.out.println("===================================================");
    }

    // Mya: reports 1–4; Aung: reports 5–8.
    private static void showCountriesMenu(
            Scanner scanner,
            CountryReports countryReports,
            PopulationReport populationReport) {

        boolean back = false;

        while (!back) {
            System.out.println("\n--- Countries Reports ---");
            System.out.println("1. All countries worldwide by population");
            System.out.println("2. All countries in a continent by population");
            System.out.println("3. All countries in a region by population");
            System.out.println("4. Population breakdown by region");
            System.out.println("5. Top N countries worldwide by population");
            System.out.println("6. Top N countries in a continent by population");
            System.out.println("7. Top N countries in a region by population");
            System.out.println("8. Language speakers and percentage of world population");
            System.out.println("0. Return to main menu");
            System.out.print("Choose a report: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" ->
                        printCountries(countryReports.getCountriesByPopulation());

                case "2" -> {
                    System.out.print("Enter a continent name: ");
                    printCountries(countryReports.getCountriesByContinent(
                            scanner.nextLine().trim()));
                }

                case "3" -> {
                    System.out.print("Enter a region name: ");
                    printCountries(countryReports.getCountriesByRegion(
                            scanner.nextLine().trim()));
                }

                case "4" ->
                        printRegionalPopulation(
                                populationReport.getPopulationByRegion());

                case "5" -> {
                    int n = readPositiveInt(scanner);
                    if (n > 0) TopCountriesWorldReport.showReport(n);
                }

                case "6" -> {
                    System.out.print("Enter a continent name: ");
                    String continent = scanner.nextLine().trim();
                    int n = readPositiveInt(scanner);
                    if (n > 0)
                        TopCountriesContinentReport.showReport(continent, n);
                }

                case "7" -> {
                    System.out.print("Enter a region name: ");
                    String region = scanner.nextLine().trim();
                    int n = readPositiveInt(scanner);
                    if (n > 0)
                        TopCountriesRegionReport.showReport(region, n);
                }

                case "8" -> LanguageSpeakersReport.showReport();

                case "0" -> back = true;

                default -> System.out.println("Invalid report choice.");
            }

            if (!back) pause(scanner);
        }
    }

    // Rosy: reports 9–14.
    private static void showCityMenu(Scanner scanner) throws SQLException {
        boolean back = false;

        while (!back) {
            System.out.println("\n--- City Reports (Rosy) ---");
            System.out.println("9. All cities worldwide by population");
            System.out.println("10. All cities in a continent by population");
            System.out.println("11. All cities in a region by population");
            System.out.println("12. All cities in a country by population");
            System.out.println("13. All cities in a district by population");
            System.out.println("14. Population breakdown by continent");
            System.out.println("0. Return to main menu");
            System.out.print("Choose a report: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "9" -> CityReport.allCitiesInWorld();

                case "10" -> {
                    System.out.print("Enter a continent name: ");
                    CityReport.allCitiesInContinent(
                            scanner.nextLine().trim());
                }

                case "11" -> {
                    System.out.print("Enter a region name: ");
                    CityReport.allCitiesInRegion(scanner.nextLine().trim());
                }

                case "12" -> {
                    System.out.print("Enter a country name: ");
                    CityReport.allCitiesInCountry(scanner.nextLine().trim());
                }

                case "13" -> {
                    System.out.print("Enter a district name: ");
                    CityReport.allCitiesInDistrict(scanner.nextLine().trim());
                }

                case "14" -> ContinentPopulationReport.populationByContinent();

                case "0" -> back = true;

                default -> System.out.println("Invalid report choice.");
            }

            if (!back) pause(scanner);
        }
    }

    // Oscar: reports 15–20.
    private static void showCapitalMenu(Scanner scanner) {
        boolean back = false;

        while (!back) {
            System.out.println("\n--- Capital Reports (Oscar) ---");
            System.out.println("15. All capital cities worldwide by population");
            System.out.println("16. All capital cities in a continent");
            System.out.println("17. All capital cities in a region");
            System.out.println("18. Top N capital cities worldwide");
            System.out.println("19. Top N capital cities in a continent");
            System.out.println("20. Top N capital cities in a region");
            System.out.println("0. Return to main menu");
            System.out.print("Choose a report: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "15" -> CapitalCityReports.showAllWorldCapitals();

                case "16" -> {
                    System.out.print("Enter a continent name: ");
                    CapitalCityReports.showAllCapitalsInContinent(
                            scanner.nextLine().trim());
                }

                case "17" -> {
                    System.out.print("Enter a region name: ");
                    CapitalCityReports.showAllCapitalsInRegion(
                            scanner.nextLine().trim());
                }

                case "18" -> {
                    int n = readPositiveInt(scanner);
                    if (n > 0) CapitalCityReports.showTopWorldCapitals(n);
                }

                case "19" -> {
                    System.out.print("Enter a continent name: ");
                    String continent = scanner.nextLine().trim();
                    int n = readPositiveInt(scanner);
                    if (n > 0)
                        CapitalCityReports.showTopCapitalsInContinent(continent, n);
                }

                case "20" -> {
                    System.out.print("Enter a region name: ");
                    String region = scanner.nextLine().trim();
                    int n = readPositiveInt(scanner);
                    if (n > 0)
                        CapitalCityReports.showTopCapitalsInRegion(region, n);
                }

                case "0" -> back = true;

                default -> System.out.println("Invalid report choice.");
            }

            if (!back) pause(scanner);
        }
    }

    // Khine: reports 21–26.
    private static void showPopulationMenu(Scanner scanner) {
        boolean back = false;

        while (!back) {
            System.out.println("\n--- Population Reports (Khine) ---");
            System.out.println("21. World population");
            System.out.println("22. Continent population");
            System.out.println("23. Region population");
            System.out.println("24. Country population");
            System.out.println("25. District population");
            System.out.println("26. City population");
            System.out.println("0. Return to main menu");
            System.out.print("Choose a report: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "21" -> WorldPopulationReport.showPopulation();

                case "22" -> {
                    System.out.print("Enter a continent name: ");
                    ContinentPopulationReport.showPopulation(
                            scanner.nextLine().trim());
                }

                case "23" -> {
                    System.out.print("Enter a region name: ");
                    RegionPopulationReport.showPopulation(
                            scanner.nextLine().trim());
                }

                case "24" -> {
                    System.out.print("Enter a country name: ");
                    CountryPopulationReport.showPopulation(
                            scanner.nextLine().trim());
                }

                case "25" -> {
                    System.out.print("Enter a district name: ");
                    DistrictPopulationReport.showPopulation(
                            scanner.nextLine().trim());
                }

                case "26" -> {
                    System.out.print("Enter a city name: ");
                    CityPopulationReport.showPopulation(
                            scanner.nextLine().trim());
                }

                case "0" -> back = true;

                default -> System.out.println("Invalid report choice.");
            }

            if (!back) pause(scanner);
        }
    }

    // Yoon: reports 27–32 are visible but not implemented.

    private static void showTopCitiesMenu(Scanner scanner) {
        boolean back = false;

        while (!back) {
            System.out.println("\n--- Cities Reports (Yoon) ---");
            System.out.println("27. Top N cities worldwide by population");
            System.out.println("28. Top N cities in a continent by population");
            System.out.println("29. Top N cities in a region by population");
            System.out.println("30. Top N cities in a country by population");
            System.out.println("31. Top N cities in a district by population");
            System.out.println("32. Population breakdown by country");
            System.out.println("0. Return to main menu");
            System.out.println("These reports have not been implemented yet.");
            System.out.print("Choose a report: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "27", "28", "29", "30", "31", "32" ->
                        System.out.println(
                                "This report has not been implemented yet.");
                case "0" -> back = true;
                default -> System.out.println("Invalid report choice.");
            }

            if (!back) {
                pause(scanner);
            }
        }
    }

    private static int readPositiveInt(Scanner scanner) {
        System.out.print("Enter the number of results (N): ");
        String input = scanner.nextLine().trim();

        try {
            int n = Integer.parseInt(input);
            if (n <= 0) {
                System.out.println("Enter a whole number greater than zero.");
                return -1;
            }
            return n;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Enter a whole number.");
            return -1;
        }
    }

    private static void pause(Scanner scanner) {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private static void printCountries(List<Country> countries) {
        if (countries.isEmpty()) {
            System.out.println("No countries found.");
            return;
        }

        System.out.printf("%-5s %-35s %-20s %-25s %15s%n",
                "Code", "Country", "Continent", "Region", "Population");
        System.out.println("-".repeat(106));

        for (Country country : countries) {
            System.out.printf("%-5s %-35s %-20s %-25s %,15d%n",
                    country.getCode(),
                    country.getName(),
                    country.getContinent(),
                    country.getRegion(),
                    country.getPopulation());
        }
    }

    private static void printRegionalPopulation(
            List<RegionPopulation> reports) {

        if (reports.isEmpty()) {
            System.out.println("No regional population data found.");
            return;
        }

        System.out.println("\n--- Population Breakdown by Region ---");
        System.out.printf("%-25s %15s %15s %15s %11s %14s%n",
                "Region", "Total", "Urban", "Non-urban",
                "Urban %", "Non-urban %");
        System.out.println("-".repeat(100));

        for (RegionPopulation report : reports) {
            System.out.printf("%-25s %,15d %,15d %,15d %10.2f%% %13.2f%%%n",
                    report.getRegion(),
                    report.getTotalPopulation(),
                    report.getUrbanPopulation(),
                    report.getNonUrbanPopulation(),
                    report.getUrbanPercentage(),
                    report.getNonUrbanPercentage());
        }
    }
}
