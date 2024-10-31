package com.petpick.domain;

import com.petpick.domain.type.OrderStatus;
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
@Table(name = "orders")
public class Orders extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private Integer orderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "orders_price")
    private Integer orderPrice;

    @Column(name = "orders_card")
    private String orderCard;

    @Enumerated(EnumType.STRING)
    @Column(name = "orders_status")
    private OrderStatus orderStatus;

    @Column(name = "orders_request")
    private String orderRequest;
}
