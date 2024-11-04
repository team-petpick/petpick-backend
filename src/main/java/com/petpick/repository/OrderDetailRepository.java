package com.petpick.repository;

import com.petpick.domain.OrderDetail;
import com.petpick.domain.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByOrdersOrdersId(Integer ordersId);
    List<OrderDetail> findByOrders(Orders orders);
}
