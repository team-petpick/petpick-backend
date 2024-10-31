package com.petpick.service.order;

import com.petpick.domain.OrderDetail;
import com.petpick.domain.Orders;
import com.petpick.global.exception.BaseException;
import com.petpick.global.exception.errorCode.OrderErrorCode;
import com.petpick.global.exception.errorCode.ProductErrorCode;
import com.petpick.model.OrderDetailResponse;
import com.petpick.model.OrderResponse;
import com.petpick.repository.OrderDetailRepository;
import com.petpick.repository.OrdersRepository;
import com.petpick.repository.ProductImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrdersRepository ordersRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductImgRepository productImgRepository;

    public Page<OrderResponse> getOrderHistory(Integer userId, Integer page, Integer month) {
        // month 파라미터 검증
        if (month != 3 && month != 6 && month != 12 && month != 36) {
            throw new BaseException(OrderErrorCode.INVALID_MONTH_PARAMETER);
        }

        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "createAt"));

        // 현재 날짜로부터 month 개월 전의 날짜를 계산
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime endDate = LocalDateTime.now(zoneId).with(LocalTime.MAX);
        LocalDateTime startDate = endDate.minusMonths(month).with(LocalTime.MIN);

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

        if(!ordersPage.hasContent()){
            throw new BaseException(ProductErrorCode.NO_PRODUCTS_AVAILABLE);
        }

        return new PageImpl<>(orderResponses, pageable, ordersPage.getTotalElements());
    }
}
