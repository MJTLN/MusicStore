package com.maciejjt.posinventory.model.requests;

import lombok.Data;

import java.util.Map;

@Data
public class StorageMovementRequest {
    Map<Long, Integer> productsQuantities;
    Long currentStorageId;
    Long newStorageId;
}
