package com.napier.sem;

import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;

            while (running) {
                System.out.println("\n=================================");
                System.out.println("       DevOps Group 8");
                System.out.println("    Population Reports");
                System.out.println("=================================");
                System.out.println("1. World population");
                System.out.println("2. Country population");
                System.out.println("3. Continent population");
                System.out.println("4. Region population");
                System.out.println("5. District population");
                System.out.println("6. City population");
                System.out.println("0. Exit");
                System.out.print("Choose a report: ");

                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1" -> WorldPopulationReport.showPopulation();

                    case "2" -> {
                        System.out.print("Enter a country name: ");
                        CountryPopulationReport.showPopulation(
                                scanner.nextLine().trim()
                        );
                    }

                    case "3" -> {
                        System.out.print("Enter a continent name: ");
                        ContinentPopulationReport.showPopulation(
                                scanner.nextLine().trim()
                        );
                    }

                    case "4" -> {
                        System.out.print("Enter a region name: ");
                        RegionPopulationReport.showPopulation(
                                scanner.nextLine().trim()
                        );
                    }

                    case "5" -> {
                        System.out.print("Enter a district name: ");
                        DistrictPopulationReport.showPopulation(
                                scanner.nextLine().trim()
                        );
                    }

                    case "6" -> {
                        System.out.print("Enter a city name: ");
                        CityPopulationReport.showPopulation(
                                scanner.nextLine().trim()
                        );
                    }

                    case "0" -> {
                        System.out.println("Goodbye!");
                        running = false;
                    }

                    default -> System.out.println(
                            "Invalid choice. Please enter a number from 0 to 6."
                    );
                }

                if (running) {
                    System.out.println("\nPress Enter to return to the menu...");
                    scanner.nextLine();
                }
            }
        }
    }
}