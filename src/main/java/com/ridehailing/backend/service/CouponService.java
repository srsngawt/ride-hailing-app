package com.ridehailing.backend.service;

import com.ridehailing.backend.domain.Coupon;
import com.ridehailing.backend.exception.RideHailingException;
import com.ridehailing.backend.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;

    public void addCoupon(String code, double discountPercentage) {
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
}
