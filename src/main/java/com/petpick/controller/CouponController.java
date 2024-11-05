package com.petpick.controller;

import com.petpick.model.CouponResponse;
import com.petpick.service.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @GetMapping
    public List<CouponResponse> getCouponsByUserId(@RequestAttribute Integer userId) {
        return couponService.getCouponsByUserId(userId);
    }
}
