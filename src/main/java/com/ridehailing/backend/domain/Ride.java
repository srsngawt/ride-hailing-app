package com.ridehailing.backend.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Ride {
    private Long id;
    private Long userId;
    private Long driverId;
    
    private Location startLocation;
    private Location endLocation;
    
    private CarType requestedCarType;
    private CarType assignedCarType; // Might be different if upgraded
    private boolean isFreeUpgrade;
    
    private RideStatus status;
    private double distance;
    private double totalFare;
    
    private String appliedCouponCode;
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
