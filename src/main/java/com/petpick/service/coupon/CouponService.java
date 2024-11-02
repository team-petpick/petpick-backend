package com.petpick.service.coupon;

import com.petpick.domain.Coupon;
import com.petpick.domain.CouponCategory;
import com.petpick.domain.User;
import com.petpick.domain.type.CouponStatus;
import com.petpick.global.exception.BaseException;
import com.petpick.global.exception.errorCode.CouponCategoryErrorCode;
import com.petpick.repository.CouponCategoryRepository;
import com.petpick.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponCategoryRepository couponCategoryRepository;

    @Transactional
    public Coupon grantCoupon(User user) {
        CouponCategory welcomeCategory = couponCategoryRepository.findById(1)
                .orElseThrow(() -> new BaseException(CouponCategoryErrorCode.COUPON_CATEGORY_NOT_FOUND));

        Optional<Coupon> hasCoupon = couponRepository.findByUserAndCouponCategory(user, welcomeCategory);


        if(hasCoupon.isEmpty()) {
            Coupon coupon = Coupon.builder()
                    .couponStatus(CouponStatus.YES)
                    .couponCategory(welcomeCategory)
                    .user(user)
                    .build();

            return couponRepository.save(coupon);
        }

        return couponRepository.save(hasCoupon.get());
    }
}
