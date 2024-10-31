package com.petpick.service.order;

import com.petpick.domain.OrderDetail;
import com.petpick.domain.Orders;
import com.petpick.model.OrderDetailResponse;
import com.petpick.model.OrderResponse;
import com.petpick.repository.OrderDetailRepository;
import com.petpick.repository.OrdersRepository;
import com.petpick.repository.ProductImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductImgRepository productImgRepository;

    public Page<OrderResponse> getOrderHistory(Integer userId, Integer page, Integer size, Integer month) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createAt"));

        // 현재 날짜로부터 month 개월 전의 날짜를 계산
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusMonths(month);

        // 수정된 리포지토리 메서드를 사용하여 주문 조회
        Page<Orders> ordersPage = ordersRepository.findByUserUserIdAndCreateAtBetween(userId, startDate, endDate, pageable);

        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Orders order : ordersPage.getContent()) {
            List<OrderDetail> orderDetails = orderDetailRepository.findByOrdersOrdersId(order.getOrdersId());
            List<OrderDetailResponse> orderDetailResponses = new ArrayList<>();

            for (OrderDetail od : orderDetails) {
                Integer productId = od.getProduct().getProductId();
                String thumbnailUrl = productImgRepository.findThumbnailByProductId(productId);
                OrderDetailResponse odr = new OrderDetailResponse(od, thumbnailUrl);
                orderDetailResponses.add(odr);
            }

            OrderResponse orderResponse = new OrderResponse(order, orderDetailResponses);
            orderResponses.add(orderResponse);
        }

        return new PageImpl<>(orderResponses, pageable, ordersPage.getTotalElements());
    }
}
