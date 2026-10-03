package com.ridehailing.backend.domain;

public enum CarType {
    HATCHBACK(1.0),
    SEDAN(1.2), // E.g., 20% premium
    SUV(1.5);   // For bonus extension

    private final double priceMultiplier;

    CarType(double priceMultiplier) {
        this.priceMultiplier = priceMultiplier;
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }
}
