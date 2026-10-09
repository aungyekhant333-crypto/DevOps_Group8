package com.napier.sem;

import java.util.List;

public class App {

    public static void main(String[] args) {

        PopulationReport populationReport = new PopulationReport();

        List<RegionPopulation> reports =
                populationReport.getPopulationByRegion();

        System.out.println("Population by Region");
        System.out.println("====================");

        for (RegionPopulation report : reports) {

            System.out.println(
                    report.getRegion() + " | " +
                            "Total: " + report.getTotalPopulation() + " | " +
                            "Urban: " + report.getUrbanPopulation() + " (" +
                            String.format("%.2f", report.getUrbanPercentage()) + "%) | " +
                            "Non-Urban: " + report.getNonUrbanPopulation() + " (" +
                            String.format("%.2f", report.getNonUrbanPercentage()) + "%)"
            );
        }
    }
}