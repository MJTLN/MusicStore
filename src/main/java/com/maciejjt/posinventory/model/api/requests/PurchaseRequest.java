package com.maciejjt.posinventory.model.api.requests;

import com.maciejjt.posinventory.model.Address;
import com.maciejjt.posinventory.model.Payment;
import com.maciejjt.posinventory.model.Product;
import com.maciejjt.posinventory.model.User;
import com.maciejjt.posinventory.model.enums.PurchaseStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
