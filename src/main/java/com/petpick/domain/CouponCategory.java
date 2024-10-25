package com.petpick.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "coupon_category")
public class CouponCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_category_id")
    private Integer couponCategoryId;

    @Column(name = "coupon_category_name")
    private String couponCategoryName;

    @Column(name = "coupon_category_discount")
    private Integer couponCategoryDiscount;

    @Column(name = "coupon_category_discount_max")
    private Integer couponCategoryDiscountMax;
}
