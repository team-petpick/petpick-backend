package com.petpick.controller;

import com.petpick.model.PaymentCancelRequest;
import com.petpick.model.PaymentSuccessRequest;
import com.petpick.service.tossPayment.TossService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.converter.HttpMessageNotReadableException;

import io.sentry.Sentry;



@RestController
@RequestMapping("/v1/payment")
public class TossPaymentController {

    private final TossService tossService;

    public TossPaymentController(TossService tossService) {
        this.tossService = tossService;
    }

    @PostMapping("/success")
    public ResponseEntity<String> paymentSuccess(@RequestBody PaymentSuccessRequest request, @RequestAttribute Integer userId) {
        // Log the received request for debugging
        System.out.println("Received request: " + request);

        Sentry.withScope(scope -> {
            scope.setTag("user_id", String.valueOf(userId));
            scope.setTag("order_serial_code", request.getOrderSerialCode());
            Sentry.captureMessage("Payment check requested for user: " + userId + ", Order Serial Code: " + request.getOrderSerialCode());
        });

        int isConfirmed = tossService.confirmPayment(request, userId);
//        boolean isConfirmed = true;
        if (isConfirmed != 0) {

            return ResponseEntity.ok(String.valueOf(isConfirmed));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment confirmation failed.");
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> paymentCancel(@RequestBody PaymentCancelRequest request, @RequestAttribute Integer userId) {
        boolean isCancelled = tossService.cancelPayment(request, userId);
        if (isCancelled) {
            return ResponseEntity.ok("Payment cancelled and order updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment cancellation failed.");
        }
    }



    // Exception handler for deserialization errors
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body("Invalid request data: " + ex.getLocalizedMessage());
    }
}
