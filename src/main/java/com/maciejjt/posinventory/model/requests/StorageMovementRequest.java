package com.maciejjt.posinventory.model.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class StorageMovementRequest {
    @NotEmpty(message = "At least one product id and quantity for it must be supplied")
    Map<Long, Integer> productsQuantities;
    @NotNull(message = "Current storage id cannot be null")
    Long currentStorageId;
    @NotNull(message = "New storage id cannot be null")
    Long newStorageId;
}