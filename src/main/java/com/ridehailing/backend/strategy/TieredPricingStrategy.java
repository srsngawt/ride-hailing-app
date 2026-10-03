package com.ridehailing.backend.strategy;

import com.ridehailing.backend.domain.Ride;
import org.springframework.stereotype.Component;

@Component
public class TieredPricingStrategy implements PricingStrategy {

    private static final double MINIMUM_FARE = 50.0;

    @Override
    public double calculateFare(Ride ride, double distanceKm) {
        double baseFare = 0.0;
        
        // Tiered logic
        // first 2 km at ₹10/km
        // 3–5 km at ₹8/km
        // 6 km and beyond at ₹5/km
        
        if (distanceKm <= 2) {
            baseFare = distanceKm * 10;
        } else if (distanceKm <= 5) {
            baseFare = (2 * 10) + ((distanceKm - 2) * 8);
        } else {
            baseFare = (2 * 10) + (3 * 8) + ((distanceKm - 5) * 5);
        }
        
        // Apply minimum fare
        baseFare = Math.max(baseFare, MINIMUM_FARE);
        
        // Apply car type multiplier
        // If it was a free upgrade (requested Hatchback, got Sedan), charge for requested car type
        double multiplier = ride.isFreeUpgrade() 
                ? ride.getRequestedCarType().getPriceMultiplier() 
                : ride.getAssignedCarType().getPriceMultiplier();
                
        return baseFare * multiplier;
    }
}
