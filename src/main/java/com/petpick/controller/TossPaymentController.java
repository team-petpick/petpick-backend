package com.petpick.controller;






import com.petpick.model.PaymentInfoResponse;
import com.petpick.service.tossPayment.TossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/payment")
public class TossPaymentController {

    @Autowired
    private TossService tossService;

    @GetMapping("/success")
    public ResponseEntity<?> handleSuccess(
            @RequestParam("orderId") String orderId,
            @RequestParam("paymentKey") String paymentKey,
            @RequestParam("amount") int amount,
            @RequestParam("user_id") int userId,
            @RequestParam("order_request") String orderRequest) {

        try {
            tossService.confirmPayment(orderId, paymentKey, amount, userId, orderRequest);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            // Handle exception, perhaps return appropriate error response
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<?> getPaymentInfo(@PathVariable String paymentId) {
        try {
            PaymentInfoResponse paymentInfo = tossService.getPaymentInfo(paymentId);
            return ResponseEntity.ok(paymentInfo);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}

