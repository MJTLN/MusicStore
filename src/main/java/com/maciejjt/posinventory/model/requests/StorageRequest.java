package com.maciejjt.posinventory.model.requests;

import com.maciejjt.posinventory.model.enums.InventoryLocationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StorageRequest {
    @NotNull
    private InventoryLocationType type;
    @NotNull
    @Size(min = 10, max = 1000, message = "Storage address must be between 10 and 1000 characters long")
    private String address;
}