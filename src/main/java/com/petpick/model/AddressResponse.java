package com.petpick.model;

import com.petpick.domain.Address;
import com.petpick.domain.type.AddressDefault;
import lombok.Data;

@Data
public class AddressResponse {
    private Integer addressId;
    private Integer userId;
    private String addressName;
    private Integer addressZipcode;
    private String addressAddr;
    private String addressAddrDetail;
    private String addressTel;
    private String addressRequest;
    private AddressDefault addressDefault;

    public AddressResponse(Address address) {
        this.addressId = address.getAddressId();
        this.userId = address.getUser() != null ? address.getUser().getUserId() : null;
        this.addressName = address.getAddressName();
        this.addressZipcode = address.getAddressZipcode();
        this.addressAddr = address.getAddressAddr();
        this.addressAddrDetail = address.getAddressAddrDetail();
        this.addressTel = address.getAddressTel();
        this.addressRequest = address.getAddressRequest();
        this.addressDefault = address.getAddressDefault();
    }
}
