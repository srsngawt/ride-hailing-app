package com.ridehailing.backend.strategy;

import com.ridehailing.backend.domain.Driver;
import com.ridehailing.backend.domain.Location;
import com.ridehailing.backend.domain.CarType;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class NearestDriverMatchingStrategy implements DriverMatchingStrategy {

    @Override
    public Optional<Driver> findBestDriver(List<Driver> availableDrivers, Location userLocation, CarType requestedCarType, double maxRadius) {
        // Find exactly matching car type first
        Optional<Driver> exactMatch = availableDrivers.stream()
                .filter(d -> d.getCarType() == requestedCarType)
                .filter(d -> d.getCurrentLocation().distanceTo(userLocation) <= maxRadius)
                .min(Comparator.comparingDouble(d -> d.getCurrentLocation().distanceTo(userLocation)));

        if (exactMatch.isPresent()) {
            return exactMatch;
        }

        // Free Upgrade Logic: If Hatchback requested but not available, try to find a Sedan
        if (requestedCarType == CarType.HATCHBACK) {
            return availableDrivers.stream()
                    .filter(d -> d.getCarType() == CarType.SEDAN)
                    .filter(d -> d.getCurrentLocation().distanceTo(userLocation) <= maxRadius)
                    .min(Comparator.comparingDouble(d -> d.getCurrentLocation().distanceTo(userLocation)));
        }

        return Optional.empty();
    }
}
