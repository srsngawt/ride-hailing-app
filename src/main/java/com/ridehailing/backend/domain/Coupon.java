package com.ridehailing.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Coupon {
    private String code;
    private double discountPercentage; // e.g., 10 for 10%
    private boolean active;
}
