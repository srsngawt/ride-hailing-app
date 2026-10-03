package com.ridehailing.backend.controller;

import com.ridehailing.backend.service.CouponService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping
    public void addCoupon(@RequestBody AddCouponRequest request) {
        couponService.addCoupon(request.code, request.discountPercentage);
    }

    @DeleteMapping("/{code}")
    public void deleteCoupon(@PathVariable String code) {
        couponService.deleteCoupon(code);
    }

    @GetMapping
    public java.util.List<com.ridehailing.backend.domain.Coupon> getAllCoupons() {
        return couponService.getAllCoupons();
    }

    @GetMapping("/{code}")
    public com.ridehailing.backend.domain.Coupon getCoupon(@PathVariable String code) {
        return couponService.getCoupon(code);
    }

        public static class AddCouponRequest {
        public String code;
        public double discountPercentage;
    }
}
