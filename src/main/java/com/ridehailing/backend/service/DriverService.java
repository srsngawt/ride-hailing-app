package com.ridehailing.backend.service;

import com.ridehailing.backend.domain.CarType;
import com.ridehailing.backend.domain.Driver;
import com.ridehailing.backend.domain.Location;
import com.ridehailing.backend.exception.RideHailingException;
import com.ridehailing.backend.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DriverService {
    private final DriverRepository driverRepository;

    public Driver registerDriver(String name, CarType carType, Location initialLocation) {
        Driver driver = Driver.builder()
                .name(name)
                .carType(carType)
                .currentLocation(initialLocation)
                .available(true)
                .build();
        return driverRepository.save(driver);
    }

    public void updateLocation(Long driverId, Location newLocation) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new RideHailingException("Driver not found"));
        driver.setCurrentLocation(newLocation);
        driverRepository.save(driver);
    }

    public Driver getDriver(Long id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new RideHailingException("Driver not found"));
    }
}
