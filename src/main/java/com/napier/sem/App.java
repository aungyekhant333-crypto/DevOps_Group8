package com.napier.sem;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;

            while (running) {
                System.out.println("\n=================================");
                System.out.println("       DevOps Group 8");
                System.out.println("=================================");
                System.out.println("1. Population Reports");
                System.out.println("2. Capital City Reports");
                System.out.println("0. Exit");
                System.out.print("Choose a menu: ");

                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1" -> showPopulationMenu(scanner);
                    case "2" -> showCapitalCityMenu(scanner);
                    case "0" -> {
                        System.out.println("Goodbye!");
                        running = false;
                    }
                    default -> System.out.println(
                            "Invalid choice. Please enter 0, 1 or 2.");
                }
            }
        }
    }

    private static void showPopulationMenu(Scanner scanner) {
        boolean back = false;

        while (!back) {
            System.out.println("\n=================================");
            System.out.println("       Population Reports");
            System.out.println("=================================");
            System.out.println("1. World population");
            System.out.println("2. Country population");
            System.out.println("3. Continent population");
            System.out.println("4. Region population");
            System.out.println("5. District population");
            System.out.println("6. City population");
            System.out.println("0. Back to main menu");
            System.out.print("Choose a report: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> WorldPopulationReport.showPopulation();

                case "2" -> {
                    System.out.print("Enter a country name: ");
                    CountryPopulationReport.showPopulation(
                            scanner.nextLine().trim());
                }

                case "3" -> {
                    System.out.print("Enter a continent name: ");
                    ContinentPopulationReport.showPopulation(
                            scanner.nextLine().trim());
                }

                case "4" -> {
                    System.out.print("Enter a region name: ");
                    RegionPopulationReport.showPopulation(
                            scanner.nextLine().trim());
                }

                case "5" -> {
                    System.out.print("Enter a district name: ");
                    DistrictPopulationReport.showPopulation(
                            scanner.nextLine().trim());
                }

                case "6" -> {
                    System.out.print("Enter a city name: ");
                    CityPopulationReport.showPopulation(
                            scanner.nextLine().trim());
                }

                case "0" -> back = true;

                default -> System.out.println(
                        "Invalid choice. Please enter a number from 0 to 6.");
            }

            if (!back) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }

    private static void showCapitalCityMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=================================");
            System.out.println("       Capital City Reports");
            System.out.println("=================================");
            System.out.println("1. All world capital cities by population");
            System.out.println("2. All capital cities in a continent");
            System.out.println("3. All capital cities in a region");
            System.out.println("4. Top N world capital cities");
            System.out.println("5. Top N capital cities in a continent");
            System.out.println("6. Top N capital cities in a region");
            System.out.println("0. Back to main menu");
            System.out.print("Choose a report: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> CapitalCityReports.showAllWorldCapitals();

                case "2" -> {
                    System.out.print("Enter a continent name: ");
                    String continent = scanner.nextLine().trim();

                    if (!continent.isEmpty()) {
                        CapitalCityReports.showAllCapitalsInContinent(
                                continent);
                    } else {
                        System.out.println("Continent name cannot be empty.");
                    }
                }

                case "3" -> {
                    System.out.print("Enter a region name: ");
                    String region = scanner.nextLine().trim();

                    if (!region.isEmpty()) {
                        CapitalCityReports.showAllCapitalsInRegion(region);
                    } else {
                        System.out.println("Region name cannot be empty.");
                    }
                }

                case "4" -> {
                    Integer n = readPositiveInteger(
                            scanner, "Enter N: ");

                    if (n != null) {
                        CapitalCityReports.showTopWorldCapitals(n);
                    }
                }

                case "5" -> {
                    System.out.print("Enter a continent name: ");
                    String continent = scanner.nextLine().trim();

                    if (continent.isEmpty()) {
                        System.out.println("Continent name cannot be empty.");
                        break;
                    }

                    Integer n = readPositiveInteger(
                            scanner, "Enter N: ");

                    if (n != null) {
                        CapitalCityReports.showTopCapitalsInContinent(
                                continent, n);
                    }
                }

                case "6" -> {
                    System.out.print("Enter a region name: ");
                    String region = scanner.nextLine().trim();

                    if (region.isEmpty()) {
                        System.out.println("Region name cannot be empty.");
                        break;
                    }

                    Integer n = readPositiveInteger(
                            scanner, "Enter N: ");

                    if (n != null) {
                        CapitalCityReports.showTopCapitalsInRegion(
                                region, n);
                    }
                }

                case "0" -> back = true;

                default -> System.out.println(
                        "Invalid choice. Please enter a number from 0 to 6.");
            }

            if (!back) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }

    private static Integer readPositiveInteger(
            Scanner scanner, String prompt) {

        System.out.print(prompt);
        String input = scanner.nextLine().trim();

        try {
            int n = Integer.parseInt(input);

            if (n > 0) {
                return n;
            }
            System.out.println("N must be greater than zero.");
        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid whole number.");
        }

        return null;
    }
}