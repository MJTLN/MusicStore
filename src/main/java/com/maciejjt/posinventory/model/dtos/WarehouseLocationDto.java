package com.maciejjt.posinventory.model.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WarehouseLocationDto {
    Long id;
    private Integer section;
    private Integer aisle;
    private Integer rack;
    private Integer shelf;
    private Integer position;
    private Integer quantity;
}
