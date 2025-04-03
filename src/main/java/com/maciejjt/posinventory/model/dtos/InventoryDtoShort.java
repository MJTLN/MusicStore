package com.maciejjt.posinventory.model.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryDtoShort implements IInventoryDto {
    private Long id;
    private Long storageId;
    private Integer quantity;
}
