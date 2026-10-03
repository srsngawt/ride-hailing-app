package com.ridehailing.backend.service;

import com.ridehailing.backend.domain.CarType;
import com.ridehailing.backend.domain.Driver;
import com.ridehailing.backend.domain.Location;
import com.ridehailing.backend.exception.RideHailingException;
import com.ridehailing.backend.repository.DriverRepository;
import org.springframework.stereotype.Service;

@Service
public class DriverService {
    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public Driver registerDriver(String name, CarType carType, Location initialLocation) {
        if (name == null || name.trim().isEmpty()) {
            throw new RideHailingException("Driver name cannot be empty");
        }
        if (carType == null) {
            throw new RideHailingException("Car type cannot be empty");
        }
        if (initialLocation == null) {
            throw new RideHailingException("Initial location cannot be empty");
        }
        Driver driver = new Driver(null, name, carType, initialLocation, true);
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

    public void deleteDriver(Long id) {
        driverRepository.findById(id).orElseThrow(() -> new RideHailingException("Driver not found"));
        driverRepository.deleteById(id);
    }
}
