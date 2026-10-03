package com.ridehailing.backend.controller;

import com.ridehailing.backend.domain.CarType;
import com.ridehailing.backend.domain.Location;
import com.ridehailing.backend.domain.Ride;
import com.ridehailing.backend.service.RideService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @PostMapping("/book")
    public Ride bookRide(@RequestBody BookRideRequest request) {
        return rideService.bookRide(
                request.getUserId(), 
                request.getStartLocation(), 
                request.getCarType(), 
                request.getCouponCode()
        );
    }

    @PostMapping("/{id}/end")
    public Ride endRide(@PathVariable Long id, @RequestBody Location endLocation) {
        return rideService.endRide(id, endLocation);
    }

    @GetMapping("/user/{userId}")
    public List<Ride> getUserHistory(@PathVariable Long userId) {
        return rideService.getUserHistory(userId);
    }

    @GetMapping("/driver/{driverId}")
    public List<Ride> getDriverHistory(@PathVariable Long driverId) {
        return rideService.getDriverHistory(driverId);
    }

    @Data
    public static class BookRideRequest {
        private Long userId;
        private Location startLocation;
        private CarType carType;
        private String couponCode;
    }
}
