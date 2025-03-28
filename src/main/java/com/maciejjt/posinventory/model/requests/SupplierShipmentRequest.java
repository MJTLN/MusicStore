package com.maciejjt.posinventory.model.requests;

import com.maciejjt.posinventory.model.enums.ShipmentStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class SupplierShipmentRequest {
    @NotNull(message = "Supplier id cannot be null")
    private Long supplierId;
    @NotNull(message = "Arrival time cannot be null")
    private LocalDateTime arrivalTime;
    @NotEmpty(message = "The shipment must contain at least one item")
    private Set<SupplierShipmentItemRequest> supplierShipmentItemRequest;
    @NotEmpty
    private BigDecimal amount;
    @Size(min = 3, max = 320, message = "The note must be between 3 and 320 characters")
    private String note;
    private ShipmentStatus status;
    @NotNull(message = "Storage id cannot be null")
    private Long storageId;
}