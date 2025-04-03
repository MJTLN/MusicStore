package com.maciejjt.posinventory.model.dtos;

import com.maciejjt.posinventory.model.enums.InventoryType;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class StorageDto {
    private Long id;
    private InventoryType type;
    private String address;
    Set<IInventoryDto> inventories;
    Set<SupplierShipmentDto> supplierShipments;
}
