package com.petpick.domain;

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
    private Integer ordersDetailId;

    @ManyToOne
    @JoinColumn(name = "orders_id")
    private Orders orders;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "order_detail_price")
    private Double orderDetailPrice;

    @Column(name = "order_detail_cnt")
    private Integer orderDetailCnt;
}
