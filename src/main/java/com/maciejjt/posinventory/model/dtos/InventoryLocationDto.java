package com.maciejjt.posinventory.model.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class InventoryLocationDto implements IinventoryLocationDto{
    private Long id;
    private Long storageId;
    private Integer quantity;
    private Set<WarehouseLocationDto> warehouseLocationDtos;
    private SupplierShipmentLastNextDto lastShipment;
    private SupplierShipmentLastNextDto nextShipment;
}
