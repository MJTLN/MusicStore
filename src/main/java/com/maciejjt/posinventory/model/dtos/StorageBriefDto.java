package com.maciejjt.posinventory.model.dtos;

import com.maciejjt.posinventory.model.enums.InventoryType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StorageBriefDto {
    private Long id;
    private InventoryType type;
    private String address;
}

