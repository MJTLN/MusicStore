package com.maciejjt.posinventory.model.dtos;

import com.maciejjt.posinventory.model.enums.InventoryLocationType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StorageBriefDto {
    private Long id;
    private InventoryLocationType type;
    private String address;
}

