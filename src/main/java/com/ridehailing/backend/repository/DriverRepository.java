package com.ridehailing.backend.repository;

import com.ridehailing.backend.domain.Driver;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class DriverRepository {
    private final Map<Long, Driver> drivers = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Driver save(Driver driver) {
        if (driver.getId() == null) {
            driver.setId(idGenerator.getAndIncrement());
        }
        drivers.put(driver.getId(), driver);
        return driver;
    }

    public Optional<Driver> findById(Long id) {
        return Optional.ofNullable(drivers.get(id));
    }

    public List<Driver> findAllAvailableDrivers() {
        return drivers.values().stream()
                .filter(Driver::isAvailable)
                .toList();
    }
}
