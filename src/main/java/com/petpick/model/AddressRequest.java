package com.petpick.model;

import com.petpick.domain.type.AddressDefault;
import lombok.Data;

@Data
public class AddressRequest {
    private Integer addressId;
    private Integer userId;
    private String addressName;
    private Integer addressZipcode;
    private String addressAddr;
    private String addressAddrDetail;
    private String addressTel;
    private String addressRequest;
    private AddressDefault addressDefault;
}
