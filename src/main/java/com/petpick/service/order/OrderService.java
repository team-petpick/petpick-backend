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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductImgRepository productImgRepository;

    public Page<OrderResponse> getOrderHistory(Integer userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createAt"));
        Page<Orders> ordersPage = ordersRepository.findByUserUserId(userId, pageable);

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
