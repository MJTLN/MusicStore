package com.maciejjt.posinventory.model.requests;

import com.maciejjt.posinventory.model.Product;
import com.maciejjt.posinventory.model.SupplierShipment;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupplierShipmentItemRequest {
    Long productId;
    private Integer quantity;
}