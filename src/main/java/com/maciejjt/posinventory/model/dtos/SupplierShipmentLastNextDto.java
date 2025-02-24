package com.maciejjt.posinventory.model.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SupplierShipmentLastNextDto {
    private Long id;
    private LocalDateTime arrivalTime;
    private Integer quantity;
}

