package com.maciejjt.posinventory.model.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WarehouseLocationRequest {
    private Integer section;
    private Integer aisle;
    private Integer rack;
    private Integer shelf;
    private Integer position;
    private Integer quantity;
}
