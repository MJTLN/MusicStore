package com.maciejjt.posinventory.model.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupplierShipmentItemRequest {
    Long productId;
    private Integer quantity;
}