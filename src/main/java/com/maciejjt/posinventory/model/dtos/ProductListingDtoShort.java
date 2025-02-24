package com.maciejjt.posinventory.model.dtos;

import com.maciejjt.posinventory.model.enums.ProductLabel;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ProductListingDtoShort implements IProductDto {
    private Long id;
    private String name;
    private Set<ProductLabel> label;
}
