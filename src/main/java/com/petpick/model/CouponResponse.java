package com.petpick.model;

import com.petpick.domain.type.CouponStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponse {
    private Integer couponId;
    private String couponCategoryName;
    private CouponStatus couponStatus;
}
