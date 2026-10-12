package com.napier.sem;

import java.util.Scanner;

public class CountryLanguageReportsMenu {

    public static void show(Scanner scanner) {

        boolean running = true;

        while (running) {

            System.out.println("\n=== Country and Language Reports ===");
            System.out.println("1. Top N countries worldwide by population");
            System.out.println("2. Top N countries in a continent by population");
            System.out.println("3. Top N countries in a region by population");
            System.out.println("4. Language speakers and percentage of world population");
            System.out.println("0. Return to main menu");
            System.out.print("Enter your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {

                case "1" -> {
                    int limit = readLimit(scanner);

                    if (limit > 0) {
                        TopCountriesWorldReport.showReport(limit);
                    }
                }

                case "2" -> {
                    System.out.print("Enter continent name: ");
                    String continent = scanner.nextLine().trim();

                    int limit = readLimit(scanner);

                    if (limit > 0) {
                        TopCountriesContinentReport.showReport(
                                continent, limit
                        );
                    }
                }

                case "3" -> {
                    System.out.print("Enter region name: ");
                    String region = scanner.nextLine().trim();

                    int limit = readLimit(scanner);

                    if (limit > 0) {
                        TopCountriesRegionReport.showReport(
                                region, limit
                        );
                    }
                }

                case "4" -> LanguageSpeakersReport.showReport();

                case "0" -> {
                    running = false;
                    System.out.println("Returning to the main menu...");
                }

                default -> System.out.println(
                        "Invalid choice. Please select an option from 0 to 4."
                );
            }
        }
    }

    private static int readLimit(Scanner scanner) {

        System.out.print("Enter the number of results (N): ");
        String input = scanner.nextLine().trim();

        try {
            int limit = Integer.parseInt(input);

            if (limit <= 0) {
                System.out.println(
                        "Please enter a number greater than zero."
                );
                return -1;
            }

            return limit;

        } catch (NumberFormatException e) {
            System.out.println(
                    "Invalid input. Please enter a whole number."
            );
            return -1;
        }
    }
}
