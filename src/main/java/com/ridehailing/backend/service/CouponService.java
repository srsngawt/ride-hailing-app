package com.ridehailing.backend.service;

import com.ridehailing.backend.domain.Coupon;
import com.ridehailing.backend.exception.RideHailingException;
import com.ridehailing.backend.repository.CouponRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CouponService {
    private final CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public void addCoupon(String code, double discountPercentage) {
        if (code == null || code.trim().isEmpty()) {
            throw new RideHailingException("Coupon code cannot be empty");
        }
        if (discountPercentage <= 0 || discountPercentage > 100) {
            throw new RideHailingException("Invalid discount percentage");
        }
        couponRepository.save(new Coupon(code, discountPercentage, true));
    }

    public void deleteCoupon(String code) {
        couponRepository.delete(code);
    }

    public Optional<Coupon> getValidCoupon(String code) {
        if (code == null || code.trim().isEmpty()) {
            return Optional.empty();
        }
        Optional<Coupon> couponOpt = couponRepository.findByCode(code);
        if (couponOpt.isPresent() && couponOpt.get().isActive()) {
            return couponOpt;
        }
        throw new RideHailingException("Invalid or inactive coupon code");
    }

    public java.util.List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public Coupon getCoupon(String code) {
        return couponRepository.findByCode(code)
                .orElseThrow(() -> new RideHailingException("Coupon not found"));
    }
}
