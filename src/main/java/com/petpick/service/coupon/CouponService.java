package com.petpick.service.coupon;

import com.petpick.domain.Coupon;
import com.petpick.domain.CouponCategory;
import com.petpick.domain.User;
import com.petpick.domain.type.CouponStatus;
import com.petpick.global.exception.BaseException;
import com.petpick.global.exception.errorCode.CouponCategoryErrorCode;
import com.petpick.global.exception.errorCode.UserErrorCode;
import com.petpick.model.CouponResponse;
import com.petpick.repository.CouponCategoryRepository;
import com.petpick.repository.CouponRepository;
import com.petpick.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponCategoryRepository couponCategoryRepository;
    private final UserRepository userRepository;

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

    @Transactional
    public List<CouponResponse> getCouponsByUserId(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BaseException(UserErrorCode.EMPTY_MEMBER));


        List<Coupon> coupons = couponRepository.findByUser(user);

        List<Integer> couponIds = coupons.stream()
                .map(Coupon::getCouponId)
                .collect(Collectors.toList());

        List<CouponResponse> couponResponses = coupons.stream()
                .map(coupon -> {
                    CouponCategory category = coupon.getCouponCategory();
                    String categoryName = (category != null) ? category.getCouponCategoryName() : "Unknown Category";

                    return CouponResponse.builder()
                            .couponId(coupon.getCouponId())
                            .couponCategoryName(categoryName)
                            .couponStatus(coupon.getCouponStatus())
                            .build();
                })
                .collect(Collectors.toList());

        // Update coupon status to NO using repository method
        if (!couponIds.isEmpty()) {
            couponRepository.updateCouponStatusByIds(CouponStatus.NO, couponIds);
        }

        return couponResponses;
    }
}
