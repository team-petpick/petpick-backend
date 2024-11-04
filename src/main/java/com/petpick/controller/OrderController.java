package com.petpick.controller;

import com.petpick.model.OrderResponse;
import com.petpick.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderService orderService;

    /*
    * Order log by User
    * */
    @GetMapping("/orders")
    public ResponseEntity<Page<OrderResponse>> getOrderHistory(
            @RequestAttribute Integer userId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "3") Integer month
    ) {
        Page<OrderResponse> orderHistory = orderService.getOrderHistory(userId, page, month);
        return ResponseEntity.ok(orderHistory);
    }
}
