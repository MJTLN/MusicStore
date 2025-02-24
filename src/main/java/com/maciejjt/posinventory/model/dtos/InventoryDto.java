package com.maciejjt.posinventory.model.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class InventoryDto {
    private Long id;
    private Integer quantity;
    private Set<InventoryLocationDto> inventoryLocations;
}
