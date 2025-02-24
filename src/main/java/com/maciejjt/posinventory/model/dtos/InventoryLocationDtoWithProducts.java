package com.maciejjt.posinventory.model.dtos;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryLocationDtoWithProducts implements IinventoryLocationDto{
    private Long id;
    private Integer quantity;
    private ProductListingDtoShort productListingDto;
    private SupplierShipmentLastNextDto lastShipment;
    private SupplierShipmentLastNextDto nextShipment;
}
