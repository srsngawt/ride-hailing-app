package com.ridehailing.backend.controller;

import com.ridehailing.backend.service.CouponService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    public void addCoupon(@RequestBody AddCouponRequest request) {
        couponService.addCoupon(request.getCode(), request.getDiscountPercentage());
    }

    @DeleteMapping("/{code}")
    public void deleteCoupon(@PathVariable String code) {
        couponService.deleteCoupon(code);
    }

    @Data
    public static class AddCouponRequest {
        private String code;
        private double discountPercentage;
    }
}
