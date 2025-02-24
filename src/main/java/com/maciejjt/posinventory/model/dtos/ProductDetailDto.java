package com.maciejjt.posinventory.model.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDetailDto {
    private String name;
    private String value;
}
