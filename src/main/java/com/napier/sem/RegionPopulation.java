package com.napier.sem;

public class RegionPopulation {

    private String region;
    private long totalPopulation;
    private long urbanPopulation;
    private long nonUrbanPopulation;
    private double urbanPercentage;
    private double nonUrbanPercentage;

    public RegionPopulation(String region,
                            long totalPopulation,
                            long urbanPopulation,
                            long nonUrbanPopulation,
                            double urbanPercentage,
                            double nonUrbanPercentage) {
        this.region = region;
        this.totalPopulation = totalPopulation;
        this.urbanPopulation = urbanPopulation;
        this.nonUrbanPopulation = nonUrbanPopulation;
        this.urbanPercentage = urbanPercentage;
        this.nonUrbanPercentage = nonUrbanPercentage;
    }

    public String getRegion() {
        return region;
    }

    public long getTotalPopulation() {
        return totalPopulation;
    }

    public long getUrbanPopulation() {
        return urbanPopulation;
    }

    public long getNonUrbanPopulation() {
        return nonUrbanPopulation;
    }

    public double getUrbanPercentage() {
        return urbanPercentage;
    }

    public double getNonUrbanPercentage() {
        return nonUrbanPercentage;
    }
}