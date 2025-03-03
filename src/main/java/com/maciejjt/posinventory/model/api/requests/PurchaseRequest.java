package com.maciejjt.posinventory.model.api.requests;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseRequest {
    private List<Long> productIds;
    private String shipper;
    private String country;
    private String city;
    private String street;
    private String house;
    private String phone;
    private String apartment;
    private String mail;
}
