package com.petpick.domain;

import com.petpick.domain.type.AddressDefault;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Integer addressId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "address_name")
    private String addressName;

    @Column(name = "address_zipcode")
    private Integer addressZipcode;

    @Column(name = "address_addr")
    private String addressAddr;

    @Column(name = "address_addr_detail")
    private String addressAddrDetail;

    @Column(name = "address_tel")
    private String addressTel;

    @Column(name = "address_request")
    private String addressRequest;

    @Enumerated(EnumType.STRING)
    @Column(name = "address_default")
    private AddressDefault addressDefault;
}
