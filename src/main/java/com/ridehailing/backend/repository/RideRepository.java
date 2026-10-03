package com.ridehailing.backend.repository;

import com.ridehailing.backend.domain.Ride;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class RideRepository {
    private final Map<Long, Ride> rides = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Ride save(Ride ride) {
        if (ride.getId() == null) {
            ride.setId(idGenerator.getAndIncrement());
        }
        rides.put(ride.getId(), ride);
        return ride;
    }

    public Optional<Ride> findById(Long id) {
        return Optional.ofNullable(rides.get(id));
    }

    public List<Ride> findByUserId(Long userId) {
        return rides.values().stream()
                .filter(ride -> ride.getUserId().equals(userId))
                .toList();
    }

    public List<Ride> findByDriverId(Long driverId) {
        return rides.values().stream()
                .filter(ride -> ride.getDriverId().equals(driverId))
                .toList();
    }
}
