package com.maciejjt.posinventory.model.api.dtos;

import com.maciejjt.posinventory.model.Payment;
import com.maciejjt.posinventory.model.Product;
import com.maciejjt.posinventory.model.User;
import com.maciejjt.posinventory.model.dtos.ProductListingDto;
import com.maciejjt.posinventory.model.dtos.ProductListingDtoShort;
import com.maciejjt.posinventory.model.enums.PaymentStatus;
import com.maciejjt.posinventory.model.enums.PurchaseStatus;
import com.maciejjt.posinventory.model.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class PurchaseDto {
    private BigDecimal amount;
    private PaymentStatus paymentStatus;
    private ShipmentStatus shipmentStatus;
    private PurchaseStatus purchaseStatus;
    private Set<ProductListingDtoShort> products;
    private String shipper;
    private String country;
    private String city;
    private String street;
    private String house;
    private String apartment;
    private String phone;
    private String mail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
