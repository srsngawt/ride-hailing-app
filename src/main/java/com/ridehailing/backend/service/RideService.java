package com.ridehailing.backend.service;

import com.ridehailing.backend.domain.*;
import com.ridehailing.backend.exception.RideHailingException;
import com.ridehailing.backend.repository.DriverRepository;
import com.ridehailing.backend.repository.RideRepository;
import com.ridehailing.backend.repository.UserRepository;
import com.ridehailing.backend.strategy.DriverMatchingStrategy;
import com.ridehailing.backend.strategy.PricingStrategy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
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

    public RideService(UserRepository userRepository, DriverRepository driverRepository, RideRepository rideRepository, CouponService couponService, DriverMatchingStrategy matchingStrategy, PricingStrategy pricingStrategy) {
        this.userRepository = userRepository;
        this.driverRepository = driverRepository;
        this.rideRepository = rideRepository;
        this.couponService = couponService;
        this.matchingStrategy = matchingStrategy;
        this.pricingStrategy = pricingStrategy;
    }

    public Ride bookRide(Long userId, Location startLocation, Location endLocation, CarType requestedCarType, String couponCode) {
        if (endLocation == null) {
            throw new RideHailingException("Destination (endLocation) is required to book a ride.");
        }

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

            Ride ride = new Ride();
            ride.setUserId(userId);
            ride.setDriverId(selectedDriver.getId());
            ride.setStartLocation(startLocation);
            ride.setEndLocation(endLocation); // Driver gets the destination
            
            ride.setRequestedCarType(requestedCarType);
            ride.setAssignedCarType(selectedDriver.getCarType());
            ride.setFreeUpgrade(isFreeUpgrade);
            
            ride.setStatus(RideStatus.ONGOING);
            ride.setStartTime(LocalDateTime.now());
            ride.setAppliedCouponCode(appliedCoupon != null ? appliedCoupon.getCode() : null);

            // Calculate ESTIMATED Fare & Distance
            double estDist = startLocation.distanceTo(endLocation);
            ride.setEstimatedDistance(estDist);
            
            double estFare = pricingStrategy.calculateFare(ride, estDist);
            if (appliedCoupon != null) {
                estFare = estFare - (estFare * (appliedCoupon.getDiscountPercentage() / 100.0));
            }
            ride.setEstimatedFare(estFare);

            return rideRepository.save(ride);
        } finally {
            bookingLock.unlock();
        }
    }

    public Ride endRide(Long rideId, Location actualEndLocation) {
        Ride ride = rideRepository.findById(rideId).orElseThrow(() -> new RideHailingException("Ride not found"));
        if (ride.getStatus() != RideStatus.ONGOING) {
            throw new RideHailingException("Ride is not ongoing");
        }

        // If actualEndLocation is not provided (e.g. driver followed maps exactly), use original endLocation
        Location finalLocation = (actualEndLocation != null) ? actualEndLocation : ride.getEndLocation();
        
        // Update ride with actual details
        ride.setEndLocation(finalLocation);
        ride.setEndTime(LocalDateTime.now());
        ride.setStatus(RideStatus.COMPLETED);
        
        double actualDistance = ride.getStartLocation().distanceTo(finalLocation);
        ride.setActualDistance(actualDistance);

        // Recalculate ACTUAL total fare based on final actual distance
        double totalFare = pricingStrategy.calculateFare(ride, actualDistance);

        // Apply coupon if exists
        if (ride.getAppliedCouponCode() != null) {
            Coupon coupon = couponService.getValidCoupon(ride.getAppliedCouponCode()).orElse(null);
            if (coupon != null) {
                totalFare = totalFare - (totalFare * (coupon.getDiscountPercentage() / 100.0));
            }
        }
        
        ride.setTotalFare(totalFare);

        // Free the driver and update their current location to the drop-off point
        Driver driver = driverRepository.findById(ride.getDriverId()).orElseThrow();
        driver.setAvailable(true);
        driver.setCurrentLocation(finalLocation);
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
