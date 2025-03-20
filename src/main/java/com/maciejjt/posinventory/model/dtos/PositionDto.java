package com.maciejjt.posinventory.model.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PositionDto {
    private Long id;
    private Long shelfId;
    private String number;
    private Integer quantity;
}