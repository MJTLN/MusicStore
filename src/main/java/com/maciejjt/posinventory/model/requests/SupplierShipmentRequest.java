package com.maciejjt.posinventory.model.requests;

import com.maciejjt.posinventory.model.enums.ShipmentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class SupplierShipmentRequest {
    private Long supplierId;
    private LocalDateTime arrivalTime;
    private Set<SupplierShipmentItemRequest> supplierShipmentItemRequest;
    private BigDecimal amount;
    private String note;
    private ShipmentStatus status;
    private Long storageId;
}