package com.maciejjt.posinventory.model.dtos;

import com.maciejjt.posinventory.model.enums.ShipmentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class SupplierShipmentDtoNoSupplier {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime arrivalTime;
    private List<SupplierShipmentItemDto> supplierShipmentItems;
    private BigDecimal amount;
    private ShipmentStatus status;
    private String note;
    private StorageBriefDto storage;
}
