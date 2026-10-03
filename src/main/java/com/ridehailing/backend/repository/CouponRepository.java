package com.ridehailing.backend.repository;

import com.ridehailing.backend.domain.Coupon;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class CouponRepository {
    private final Map<String, Coupon> coupons = new ConcurrentHashMap<>();

    public void save(Coupon coupon) {
        coupons.put(coupon.getCode(), coupon);
    }

    public void delete(String code) {
        coupons.remove(code);
    }

    public Optional<Coupon> findByCode(String code) {
        return Optional.ofNullable(coupons.get(code));
    }

    public java.util.List<Coupon> findAll() {
        return new java.util.ArrayList<>(coupons.values());
    }
}
