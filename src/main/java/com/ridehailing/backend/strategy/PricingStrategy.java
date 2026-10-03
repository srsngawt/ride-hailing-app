package com.ridehailing.backend.strategy;

import com.ridehailing.backend.domain.Ride;

public interface PricingStrategy {
    double calculateFare(Ride ride, double distanceKm);
}
