package com.maciejjt.posinventory.model.dtos;

import com.maciejjt.posinventory.model.enums.ShipmentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
public class SupplierShipmentDto {
    private Long id;
    private SupplierDtoWithShipments supplier;
    private LocalDateTime createdAt;
    private LocalDateTime arrivalTime;
    private Map<IProductDto, Integer> supplierShipmentItems;
    private BigDecimal amount;
    private ShipmentStatus status;
    private String note;
    private StorageBriefDto storage;
}
