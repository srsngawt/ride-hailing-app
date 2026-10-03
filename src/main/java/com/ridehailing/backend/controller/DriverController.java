package com.ridehailing.backend.controller;

import com.ridehailing.backend.domain.CarType;
import com.ridehailing.backend.domain.Driver;
import com.ridehailing.backend.domain.Location;
import com.ridehailing.backend.service.DriverService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(DriverService driverService) {
        this.driverService = driverService;
    }

    @PostMapping("/register")
    public Driver registerDriver(@RequestBody RegisterDriverRequest request) {
        return driverService.registerDriver(request.name, request.carType, request.initialLocation);
    }

    @PutMapping("/{id}/location")
    public void updateLocation(@PathVariable Long id, @RequestBody Location newLocation) {
        driverService.updateLocation(id, newLocation);
    }

    @GetMapping("/{id}")
    public Driver getDriver(@PathVariable Long id) {
        return driverService.getDriver(id);
    }

    @GetMapping("/{id}/location")
    public Location getDriverLocation(@PathVariable Long id) {
        return driverService.getDriver(id).getCurrentLocation();
    }

    @DeleteMapping("/{id}")
    public void deleteDriver(@PathVariable Long id) {
        driverService.deleteDriver(id);
    }

        public static class RegisterDriverRequest {
        public String name;
        public CarType carType;
        public Location initialLocation;
    }
}
