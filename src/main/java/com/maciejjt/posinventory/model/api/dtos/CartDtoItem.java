package com.maciejjt.posinventory.model.api.dtos;

import com.maciejjt.posinventory.model.dtos.ProductListingDtoShort;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CartDtoItem {
    ProductListingDtoShort product;
    Integer Quantity;
}
