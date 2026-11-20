
package com.napier.sem;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        CountryReports countryReports = new CountryReports();
        PopulationReport populationReport = new PopulationReport();

        // Run all reports automatically when launched in batch mode.
        if (args.length > 0 && "--batch".equals(args[0])) {
            try {
                runBatchReports(countryReports, populationReport);
            } catch (SQLException e) {
                System.err.println("Batch report execution failed.");
                System.err.println("Database error: " + e.getMessage());
                System.exit(1);
            }
            return;
        }

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

    /**
     * Executes all 32 reports automatically using predefined parameters.
     * This mode allows the application to run without interactive input,
     * including in a GitHub Actions workflow.
     */
    private static void runBatchReports(
            CountryReports countryReports,
            PopulationReport populationReport) throws SQLException {

        // Predefined parameters for automated execution.
        final String CONTINENT = "Asia";
        final String REGION = "Southeast Asia";
        final String COUNTRY = "Myanmar";
        final String DISTRICT = "Yangon";
        final String CITY = "Yangon";
        final int TOP_N = 10;

        System.out.println("==============================================");
        System.out.println(" DEVOPS GROUP 8 - AUTOMATED REPORT EXECUTION");
        System.out.println("==============================================");

        System.out.println("\n--- Countries Reports ---");

        System.out.println("\nReport 1: All countries worldwide");
        printCountries(countryReports.getCountriesByPopulation());

        System.out.println("\nReport 2: Countries in " + CONTINENT);
        printCountries(countryReports.getCountriesByContinent(CONTINENT));

        System.out.println("\nReport 3: Countries in " + REGION);
        printCountries(countryReports.getCountriesByRegion(REGION));

        System.out.println("\nReport 4: Population breakdown by region");
        printRegionalPopulation(populationReport.getPopulationByRegion());

        System.out.println("\nReport 5: Top countries worldwide");
        TopCountriesWorldReport.showReport(TOP_N);

        System.out.println("\nReport 6: Top countries in " + CONTINENT);
        TopCountriesContinentReport.showReport(CONTINENT, TOP_N);

        System.out.println("\nReport 7: Top countries in " + REGION);
        TopCountriesRegionReport.showReport(REGION, TOP_N);

        System.out.println("\nReport 8: Language speakers");
        LanguageSpeakersReport.showReport();

        System.out.println("\n--- City Reports ---");

        System.out.println("\nReport 9: All cities worldwide");
        CityReport.allCitiesInWorld();

        System.out.println("\nReport 10: Cities in " + CONTINENT);
        CityReport.allCitiesInContinent(CONTINENT);

        System.out.println("\nReport 11: Cities in " + REGION);
        CityReport.allCitiesInRegion(REGION);

        System.out.println("\nReport 12: Cities in " + COUNTRY);
        CityReport.allCitiesInCountry(COUNTRY);

        System.out.println("\nReport 13: Cities in " + DISTRICT);
        CityReport.allCitiesInDistrict(DISTRICT);

        System.out.println("\nReport 14: Population by continent");
        ContinentPopulationReport.populationByContinent();

        System.out.println("\n--- Capital Reports ---");

        System.out.println("\nReport 15: All world capitals");
        CapitalCityReports.showAllWorldCapitals();

        System.out.println("\nReport 16: Capitals in " + CONTINENT);
        CapitalCityReports.showAllCapitalsInContinent(CONTINENT);

        System.out.println("\nReport 17: Capitals in " + REGION);
        CapitalCityReports.showAllCapitalsInRegion(REGION);

        System.out.println("\nReport 18: Top world capitals");
        CapitalCityReports.showTopWorldCapitals(TOP_N);

        System.out.println("\nReport 19: Top capitals in " + CONTINENT);
        CapitalCityReports.showTopCapitalsInContinent(CONTINENT, TOP_N);

        System.out.println("\nReport 20: Top capitals in " + REGION);
        CapitalCityReports.showTopCapitalsInRegion(REGION, TOP_N);

        System.out.println("\n--- Population Reports ---");

        System.out.println("\nReport 21: World population");
        WorldPopulationReport.showPopulation();

        System.out.println("\nReport 22: " + CONTINENT + " population");
        ContinentPopulationReport.showPopulation(CONTINENT);

        System.out.println("\nReport 23: " + REGION + " population");
        RegionPopulationReport.showPopulation(REGION);

        System.out.println("\nReport 24: " + COUNTRY + " population");
        CountryPopulationReport.showPopulation(COUNTRY);

        System.out.println("\nReport 25: " + DISTRICT + " population");
        DistrictPopulationReport.showPopulation(DISTRICT);

        System.out.println("\nReport 26: " + CITY + " population");
        CityPopulationReport.showPopulation(CITY);

        System.out.println("\n--- Top Cities Reports ---");

        System.out.println("\nReport 27: Top cities worldwide");
        TopCitiesWorldReport.showReport(TOP_N);

        System.out.println("\nReport 28: Top cities in " + CONTINENT);
        TopCitiesContinentReport.showReport(CONTINENT, TOP_N);

        System.out.println("\nReport 29: Top cities in " + REGION);
        TopCitiesRegionReport.showReport(REGION, TOP_N);

        System.out.println("\nReport 30: Top cities in " + COUNTRY);
        TopCitiesCountryReport.showReport(COUNTRY, TOP_N);

        System.out.println("\nReport 31: Top cities in " + DISTRICT);
        TopCitiesDistrictReport.showReport(DISTRICT, TOP_N);

        System.out.println("\nReport 32: Population breakdown by country");
        CountryPopulationBreakdownReport.showReport();

        System.out.println("\n==============================================");
        System.out.println(" AUTOMATED REPORT EXECUTION FINISHED");
        System.out.println("==============================================");
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


    // Yoon: reports 27–32.
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
            System.out.print("Choose a report: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "27" -> {
                    int n = readPositiveInt(scanner);
                    if (n > 0) {
                        TopCitiesWorldReport.showReport(n);
                    }
                }

                case "28" -> {
                    System.out.print("Enter a continent name: ");
                    String continent = scanner.nextLine().trim();
                    int n = readPositiveInt(scanner);
                    if (n > 0) {
                        TopCitiesContinentReport.showReport(continent, n);
                    }
                }

                case "29" -> {
                    System.out.print("Enter a region name: ");
                    String region = scanner.nextLine().trim();
                    int n = readPositiveInt(scanner);
                    if (n > 0) {
                        TopCitiesRegionReport.showReport(region, n);
                    }
                }

                case "30" -> {
                    System.out.print("Enter a country name: ");
                    String country = scanner.nextLine().trim();
                    int n = readPositiveInt(scanner);
                    if (n > 0) {
                        TopCitiesCountryReport.showReport(country, n);
                    }
                }

                case "31" -> {
                    System.out.print("Enter a district name: ");
                    String district = scanner.nextLine().trim();
                    int n = readPositiveInt(scanner);
                    if (n > 0) {
                        TopCitiesDistrictReport.showReport(district, n);
                    }
                }

                case "32" -> CountryPopulationBreakdownReport.showReport();

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
