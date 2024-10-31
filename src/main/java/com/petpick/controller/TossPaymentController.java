package com.petpick.controller;

import com.petpick.model.PaymentSuccessRequest;
import com.petpick.service.tossPayment.TossService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class TossPaymentController {

    private final TossService tossService;

    public TossPaymentController(TossService tossService) {
        this.tossService = tossService;
    }

    @PostMapping("/success")
    public ResponseEntity<String> paymentSuccess(@RequestBody PaymentSuccessRequest request) {
        boolean isConfirmed = tossService.confirmPayment(request);

        if (isConfirmed) {
            return ResponseEntity.ok("Payment confirmed and order saved successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment confirmation failed.");
        }
    }
}
