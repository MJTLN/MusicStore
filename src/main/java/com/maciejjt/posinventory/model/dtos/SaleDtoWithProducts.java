package com.maciejjt.posinventory.model.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class SaleDtoWithProducts {
    private Long id;
    private String name;
    private String description;
    private Set<ProductListingDto> products;
}
