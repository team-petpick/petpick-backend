package com.petpick.repository;

import com.petpick.domain.Coupon;
import com.petpick.domain.CouponCategory;
import com.petpick.domain.User;
import com.petpick.domain.type.CouponStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {
    List<Coupon> findByUser(User user);

    Optional<Coupon> findByUserAndCouponCategory(User user, CouponCategory couponCategory);

    @Modifying
    @Query("UPDATE Coupon c SET c.couponStatus = :status WHERE c.couponId IN :couponIds")
    void updateCouponStatusByIds(CouponStatus status, List<Integer> couponIds);
}
