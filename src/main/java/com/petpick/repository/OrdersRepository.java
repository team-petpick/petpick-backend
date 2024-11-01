package com.petpick.repository;

import com.petpick.domain.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    Page<Orders> findByUserUserIdAndCreateAtBetween(Integer userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);}
