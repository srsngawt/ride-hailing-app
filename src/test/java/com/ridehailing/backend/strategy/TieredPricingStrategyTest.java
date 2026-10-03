package com.ridehailing.backend.strategy;

import com.ridehailing.backend.domain.CarType;
import com.ridehailing.backend.domain.Ride;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TieredPricingStrategyTest {

    private TieredPricingStrategy pricingStrategy;

    @BeforeEach
    void setUp() {
        pricingStrategy = new TieredPricingStrategy();
    }

    @Test
    void testMinimumFare() {
        Ride ride = Ride.builder()
                .assignedCarType(CarType.HATCHBACK) // 1.0 multiplier
                .isFreeUpgrade(false)
                .build();
        
        // 1 km should be 1 * 10 = 10, but min fare is 50
        double fare = pricingStrategy.calculateFare(ride, 1.0);
        assertEquals(50.0, fare);
    }

    @Test
    void testTieredPricing_FirstTier() {
        Ride ride = Ride.builder()
                .assignedCarType(CarType.HATCHBACK)
                .isFreeUpgrade(false)
                .build();
        
        // 2 km = 2 * 10 = 20, min fare 50 applies
        double fare = pricingStrategy.calculateFare(ride, 2.0);
        assertEquals(50.0, fare);
    }

    @Test
    void testTieredPricing_SecondTier() {
        Ride ride = Ride.builder()
                .assignedCarType(CarType.HATCHBACK)
                .isFreeUpgrade(false)
                .build();
        
        // 4 km = (2 * 10) + (2 * 8) = 20 + 16 = 36, min fare 50 applies
        double fare = pricingStrategy.calculateFare(ride, 4.0);
        assertEquals(50.0, fare);
    }

    @Test
    void testTieredPricing_ThirdTier() {
        Ride ride = Ride.builder()
                .assignedCarType(CarType.HATCHBACK)
                .isFreeUpgrade(false)
                .build();
        
        // 10 km = (2 * 10) + (3 * 8) + (5 * 5) = 20 + 24 + 25 = 69
        double fare = pricingStrategy.calculateFare(ride, 10.0);
        assertEquals(69.0, fare);
    }

    @Test
    void testCarTypePremiumRate() {
        Ride ride = Ride.builder()
                .assignedCarType(CarType.SEDAN) // 1.2 multiplier
                .isFreeUpgrade(false)
                .build();
        
        // 10 km = 69 base fare. 69 * 1.2 = 82.8
        double fare = pricingStrategy.calculateFare(ride, 10.0);
        assertEquals(82.8, fare, 0.01);
    }

    @Test
    void testFreeUpgradePricing() {
        Ride ride = Ride.builder()
                .requestedCarType(CarType.HATCHBACK) // 1.0 multiplier
                .assignedCarType(CarType.SEDAN)      // 1.2 multiplier, but should be ignored
                .isFreeUpgrade(true)
                .build();
        
        // 10 km = 69 base fare. Should multiply by HATCHBACK (1.0), not SEDAN
        double fare = pricingStrategy.calculateFare(ride, 10.0);
        assertEquals(69.0, fare);
    }
}
