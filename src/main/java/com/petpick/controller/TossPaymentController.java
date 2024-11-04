package com.petpick.controller;

import com.petpick.model.PaymentSuccessRequest;
import com.petpick.service.tossPayment.TossService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.converter.HttpMessageNotReadableException;

@RestController
@RequestMapping("/payment")
public class TossPaymentController {

    private final TossService tossService;

    public TossPaymentController(TossService tossService) {
        this.tossService = tossService;
    }

    @PostMapping("/success")
    public ResponseEntity<String> paymentSuccess(@RequestBody PaymentSuccessRequest request) {
        // Log the received request for debugging
        System.out.println("Received request: " + request);

        boolean isConfirmed = tossService.confirmPayment(request);

        if (isConfirmed) {
            return ResponseEntity.ok("Payment confirmed and order saved successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment confirmation failed.");
        }
    }

    // Exception handler for deserialization errors
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body("Invalid request data: " + ex.getLocalizedMessage());
    }
}
