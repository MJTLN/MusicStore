package com.maciejjt.posinventory.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class WarehouseLayoutIdDto {
    private Long id;
    private String number;
}
