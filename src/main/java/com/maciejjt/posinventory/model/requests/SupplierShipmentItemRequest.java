package com.maciejjt.posinventory.model.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SupplierShipmentItemRequest {
    @NotNull(message = "Product id in item request cannot be null")
    Long productId;
    @NotNull(message = "Quantity in item request cannot be null")
    private Integer quantity;
}