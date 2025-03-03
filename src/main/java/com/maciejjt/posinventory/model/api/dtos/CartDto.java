package com.maciejjt.posinventory.model.api.dtos;

import com.maciejjt.posinventory.model.dtos.ProductListingDtoShort;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class CartDto {
    Long id;
    Map<ProductListingDtoShort, Integer> products;
}
