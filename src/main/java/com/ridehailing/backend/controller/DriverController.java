package com.ridehailing.backend.controller;

import com.ridehailing.backend.domain.CarType;
import com.ridehailing.backend.domain.Driver;
import com.ridehailing.backend.domain.Location;
import com.ridehailing.backend.service.DriverService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @PostMapping("/register")
    public Driver registerDriver(@RequestBody RegisterDriverRequest request) {
        return driverService.registerDriver(request.getName(), request.getCarType(), request.getInitialLocation());
    }

    @PutMapping("/{id}/location")
    public void updateLocation(@PathVariable Long id, @RequestBody Location newLocation) {
        driverService.updateLocation(id, newLocation);
    }

    @Data
    public static class RegisterDriverRequest {
        private String name;
        private CarType carType;
        private Location initialLocation;
    }
}
