package com.ridehailing.backend.service;

import com.ridehailing.backend.domain.*;
import com.ridehailing.backend.exception.RideHailingException;
import com.ridehailing.backend.repository.DriverRepository;
import com.ridehailing.backend.repository.RideRepository;
import com.ridehailing.backend.repository.UserRepository;
import com.ridehailing.backend.strategy.DriverMatchingStrategy;
import com.ridehailing.backend.strategy.PricingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class RideService {

    private final UserRepository userRepository;
    private final DriverRepository driverRepository;
    private final RideRepository rideRepository;
    private final CouponService couponService;
    
    private final DriverMatchingStrategy matchingStrategy;
    private final PricingStrategy pricingStrategy;

    private static final double MAX_SEARCH_RADIUS = 5.0; // Assume 5 units/km radius
    
    // Lock for concurrency safety during booking
    private final Lock bookingLock = new ReentrantLock();

    public Ride bookRide(Long userId, Location startLocation, CarType requestedCarType, String couponCode) {
        // Validate user
        userRepository.findById(userId).orElseThrow(() -> new RideHailingException("User not found"));
        
        // Validate coupon early
        Coupon appliedCoupon = couponService.getValidCoupon(couponCode).orElse(null);

        bookingLock.lock(); // Concurrency safety: handle race condition when two users try to book at the same time
        try {
            List<Driver> availableDrivers = driverRepository.findAllAvailableDrivers();
            
            Driver selectedDriver = matchingStrategy.findBestDriver(availableDrivers, startLocation, requestedCarType, MAX_SEARCH_RADIUS)
                    .orElseThrow(() -> new RideHailingException("No drivers available in radius"));

            // Mark driver as unavailable
            selectedDriver.setAvailable(false);
            driverRepository.save(selectedDriver);

            boolean isFreeUpgrade = (requestedCarType == CarType.HATCHBACK && selectedDriver.getCarType() == CarType.SEDAN);

            Ride ride = Ride.builder()
                    .userId(userId)
                    .driverId(selectedDriver.getId())
                    .startLocation(startLocation)
                    .requestedCarType(requestedCarType)
                    .assignedCarType(selectedDriver.getCarType())
                    .isFreeUpgrade(isFreeUpgrade)
                    .status(RideStatus.ONGOING)
                    .startTime(LocalDateTime.now())
                    .appliedCouponCode(appliedCoupon != null ? appliedCoupon.getCode() : null)
                    .build();

            return rideRepository.save(ride);
        } finally {
            bookingLock.unlock();
        }
    }

    public Ride endRide(Long rideId, Location endLocation) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new RideHailingException("Ride not found"));
        if (ride.getStatus() != RideStatus.ONGOING) {
            throw new RideHailingException("Ride is not ongoing");
        }

        ride.setEndLocation(endLocation);
        ride.setEndTime(LocalDateTime.now());
        ride.setStatus(RideStatus.COMPLETED);
        
        double distance = ride.getStartLocation().distanceTo(endLocation);
        ride.setDistance(distance);

        double totalFare = pricingStrategy.calculateFare(ride, distance);

        // Apply coupon if exists
        if (ride.getAppliedCouponCode() != null) {
            Coupon coupon = couponService.getValidCoupon(ride.getAppliedCouponCode()).orElse(null);
            if (coupon != null) {
                totalFare = totalFare - (totalFare * (coupon.getDiscountPercentage() / 100.0));
            }
        }
        
        ride.setTotalFare(totalFare);

        // Free the driver
        Driver driver = driverRepository.findById(ride.getDriverId()).orElseThrow();
        driver.setAvailable(true);
        driverRepository.save(driver);

        return rideRepository.save(ride);
    }

    public List<Ride> getUserHistory(Long userId) {
        return rideRepository.findByUserId(userId);
    }

    public List<Ride> getDriverHistory(Long driverId) {
        return rideRepository.findByDriverId(driverId);
    }
}
