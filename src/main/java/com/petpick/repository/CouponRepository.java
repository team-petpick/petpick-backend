package com.petpick.repository;

import com.petpick.domain.Coupon;
import com.petpick.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    Optional<Coupon> findByUserAndCouponCategoryId(User user, Integer couponCategoryId);
}
