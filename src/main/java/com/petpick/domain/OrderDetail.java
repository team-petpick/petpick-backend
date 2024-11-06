package com.petpick.domain;

import com.petpick.domain.type.OrderDetailStatus;
import com.petpick.global.entity.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_detail")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Integer orderDetailId;

    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Orders orders;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "order_detail_price")
    private Integer orderDetailPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_detail_status")
    private OrderDetailStatus orderDetailStatus;

    @Column(name = "order_detail_cnt")
    private Integer orderDetailCnt;

    // Existing setter for orderDetailStatus
    public void setOrderDetailStatus(OrderDetailStatus orderDetailStatus) {
        this.orderDetailStatus = orderDetailStatus;
    }

    public void decreaseOrderDetailCnt(int amount) {
        if (this.orderDetailCnt == null) {
            throw new IllegalStateException("Order detail count is not initialized.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to decrease must be positive.");
        }
        if (this.orderDetailCnt < amount) {
            throw new IllegalArgumentException("Cannot decrease by more than the current count.");
        }
        this.orderDetailCnt -= amount;
    }

    // Method to check if orderDetailCnt is zero
    public boolean isOrderDetailCntZero() {
        return this.orderDetailCnt != null && this.orderDetailCnt == 0;
    }
}
