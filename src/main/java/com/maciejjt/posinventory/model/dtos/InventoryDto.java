package com.maciejjt.posinventory.model.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class InventoryDto implements IInventoryDto {
    private Long id;
    private Long storageId;
    private Integer quantity;
    private SupplierShipmentLastNextDto lastShipment;
    private SupplierShipmentLastNextDto nextShipment;
}
