package com.maciejjt.posinventory.model.requests;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryRequest {
    private Long storageId;
    private Integer quantity;
}
