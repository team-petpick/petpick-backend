package com.petpick.controller;

import com.petpick.domain.Orders;
import com.petpick.model.OrderDetailResponse;
import com.petpick.model.OrderResponse;
import com.petpick.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
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

    /**
     * Order log after payment
     * */
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<List<OrderDetailResponse>> getOrder(
            @PathVariable Integer orderId
    ) {
        Orders orders = orderService.getOrders(orderId);

        List<OrderDetailResponse> orderDetailResponse = orderService.getOrdersByOrdersSerialCode(orders);
        return ResponseEntity.ok(orderDetailResponse);
    }
}
