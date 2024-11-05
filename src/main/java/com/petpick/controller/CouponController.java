package com.petpick.controller;

import com.petpick.model.CouponResponse;
import com.petpick.service.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    @GetMapping("/{userId}")
    public List<CouponResponse> getCouponsByUserId(@PathVariable Integer userId) {
        return couponService.getCouponsByUserId(userId);
    }
}
