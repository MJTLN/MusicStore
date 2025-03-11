package com.maciejjt.posinventory.model.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WarehouseLocationDto {
    private Long id;
    private String number;
    private Integer quantity;
}
