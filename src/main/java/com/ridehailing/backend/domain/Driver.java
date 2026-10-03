package com.ridehailing.backend.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Driver {
    private Long id;
    private String name;
    private CarType carType;
    private Location currentLocation;
    private boolean available;
}
