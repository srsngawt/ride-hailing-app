package com.ridehailing.backend.domain;

import java.time.LocalDateTime;

public class Ride {
    private Long id;
    private Long userId;
    private Long driverId;
    
    private Location startLocation;
    private Location endLocation; // Now captured at booking
    
    private CarType requestedCarType;
    private CarType assignedCarType;
    private boolean isFreeUpgrade;
    
    private RideStatus status;
    private double estimatedDistance;
    private double estimatedFare; // Shown at booking
    
    private double actualDistance; // Calculated at end
    private double totalFare;      // Final bill
    
    private String appliedCouponCode;
    
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Ride() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }
    public Location getStartLocation() { return startLocation; }
    public void setStartLocation(Location startLocation) { this.startLocation = startLocation; }
    public Location getEndLocation() { return endLocation; }
    public void setEndLocation(Location endLocation) { this.endLocation = endLocation; }
    public CarType getRequestedCarType() { return requestedCarType; }
    public void setRequestedCarType(CarType requestedCarType) { this.requestedCarType = requestedCarType; }
    public CarType getAssignedCarType() { return assignedCarType; }
    public void setAssignedCarType(CarType assignedCarType) { this.assignedCarType = assignedCarType; }
    public boolean isFreeUpgrade() { return isFreeUpgrade; }
    public void setFreeUpgrade(boolean freeUpgrade) { this.isFreeUpgrade = freeUpgrade; }
    public RideStatus getStatus() { return status; }
    public void setStatus(RideStatus status) { this.status = status; }
    
    public double getEstimatedDistance() { return estimatedDistance; }
    public void setEstimatedDistance(double estimatedDistance) { this.estimatedDistance = estimatedDistance; }
    public double getEstimatedFare() { return estimatedFare; }
    public void setEstimatedFare(double estimatedFare) { this.estimatedFare = estimatedFare; }
    
    public double getActualDistance() { return actualDistance; }
    public void setActualDistance(double actualDistance) { this.actualDistance = actualDistance; }
    public double getTotalFare() { return totalFare; }
    public void setTotalFare(double totalFare) { this.totalFare = totalFare; }
    
    public String getAppliedCouponCode() { return appliedCouponCode; }
    public void setAppliedCouponCode(String appliedCouponCode) { this.appliedCouponCode = appliedCouponCode; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
}
