package com.maciejjt.posinventory.model.dtos;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryDtoWithProducts implements IInventoryDto {
    private Long id;
    private Integer quantity;
    private ProductListingDtoShort productListingDto;
    private SupplierShipmentLastNextDto lastShipment;
    private SupplierShipmentLastNextDto nextShipment;
}
