package com.petpick.domain;

import com.petpick.domain.type.CouponStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupon")
public class Coupon { // 쿠폰 발급 내역 역할 + 쿠폰 카테고리와 사용자 매핑 테이블
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private Integer couponId;

    @ManyToOne
    @JoinColumn(name = "coupon_category_id")
    private CouponCategory couponCategory;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "coupon_status")
    private CouponStatus couponStatus;
}
