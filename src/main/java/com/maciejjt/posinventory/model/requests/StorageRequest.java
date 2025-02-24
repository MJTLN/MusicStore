package com.maciejjt.posinventory.model.requests;

import com.maciejjt.posinventory.model.enums.InventoryLocationType;
import lombok.Data;

@Data
public class StorageRequest {
    private InventoryLocationType type;
    private String address;
}
