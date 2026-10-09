package com.napier.sem;

import java.util.List;

public class App {

    public static void main(String[] args) {

        CountryReports countryReports = new CountryReports();

        List<Country> countries =
                countryReports.getCountriesByPopulation();

        System.out.println("All Countries by Population");
        System.out.println("============================");

        for (Country country : countries) {
            System.out.println(
                    country.getName() + " | " +
                            country.getPopulation()
            );
        }
    }
}