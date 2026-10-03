package com.ridehailing.backend.strategy;

import com.ridehailing.backend.domain.Driver;
import com.ridehailing.backend.domain.Location;
import com.ridehailing.backend.domain.CarType;
import java.util.List;
import java.util.Optional;

public interface DriverMatchingStrategy {
    Optional<Driver> findBestDriver(List<Driver> availableDrivers, Location userLocation, CarType requestedCarType, double maxRadius);
}
