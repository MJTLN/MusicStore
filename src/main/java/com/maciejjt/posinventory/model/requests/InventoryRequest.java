package com.maciejjt.posinventory.model.requests;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InventoryRequest {
    @NotNull(message = "Storage id cannot be null")
    private Long storageId;
    @NotNull(message = "Quantity cannot be null")
    private Integer quantity;
}