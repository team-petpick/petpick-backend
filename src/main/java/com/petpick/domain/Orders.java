package com.petpick.domain;

import com.petpick.domain.type.OrderStatus;
import com.petpick.global.entity.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Orders extends BaseTime{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private Integer ordersId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "orders_price")
    private Integer ordersPrice;

    @Column(name = "payment_key")
    private String paymentKey;

    @Column(name = "orders_request")
    private String ordersRequest;

    @Column(name = "orders_serial_code")
    private String ordersSerialCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "orders_status")
    private OrderStatus ordersStatus;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;

    public void decreaseOrdersPrice(int amount) {
        if (this.ordersPrice == null) {
            throw new IllegalStateException("Order price is not initialized.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount to decrease must be positive.");
        }
        if (this.ordersPrice < amount) {
            throw new IllegalArgumentException("Cannot decrease by more than the current order price.");
        }
        this.ordersPrice -= amount;
    }

}
