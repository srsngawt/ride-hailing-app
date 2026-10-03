package com.ridehailing.backend.domain;

public class Driver {
    private Long id;
    private String name;
    private CarType carType;
    private Location currentLocation;
    private boolean available;

    public Driver() {}
    public Driver(Long id, String name, CarType carType, Location currentLocation, boolean available) {
        this.id = id; this.name = name; this.carType = carType; this.currentLocation = currentLocation; this.available = available;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public CarType getCarType() { return carType; }
    public void setCarType(CarType carType) { this.carType = carType; }
    public Location getCurrentLocation() { return currentLocation; }
    public void setCurrentLocation(Location currentLocation) { this.currentLocation = currentLocation; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
