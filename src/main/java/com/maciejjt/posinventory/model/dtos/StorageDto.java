package com.maciejjt.posinventory.model.dtos;

import com.maciejjt.posinventory.model.enums.InventoryLocationType;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class StorageDto {
    private Long id;
    private InventoryLocationType type;
    private String address;
    Set<IinventoryLocationDto> inventoryLocations;
    Set<SupplierShipmentDto> supplierShipments; //usunac? możę oddzielny request na to tak samo na movements?
}
