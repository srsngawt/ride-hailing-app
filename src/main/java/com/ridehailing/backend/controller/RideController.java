package com.ridehailing.backend.controller;

import com.ridehailing.backend.domain.CarType;
import com.ridehailing.backend.domain.Location;
import com.ridehailing.backend.domain.Ride;
import com.ridehailing.backend.service.RideService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideService rideService;

    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping("/book")
    public Ride bookRide(@RequestBody BookRideRequest request) {
        return rideService.bookRide(
                request.userId, 
                request.startLocation, 
                request.endLocation, // Now accepting end location at booking
                request.carType, 
                request.couponCode
        );
    }

    @PostMapping("/{id}/end")
    public Ride endRide(@PathVariable Long id, @RequestBody(required = false) Location actualEndLocation) {
        return rideService.endRide(id, actualEndLocation);
    }

    @GetMapping("/user/{userId}")
    public List<Ride> getUserHistory(@PathVariable Long userId) {
        return rideService.getUserHistory(userId);
    }

    @GetMapping("/driver/{driverId}")
    public List<Ride> getDriverHistory(@PathVariable Long driverId) {
        return rideService.getDriverHistory(driverId);
    }

    public static class BookRideRequest {
        public Long userId;
        public Location startLocation;
        public Location endLocation; // Destination required for booking
        public CarType carType;
        public String couponCode;
    }
}
