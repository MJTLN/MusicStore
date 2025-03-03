package com.maciejjt.posinventory.model.api.dtos;

import com.maciejjt.posinventory.model.dtos.DiscountDto;
import com.maciejjt.posinventory.model.dtos.ProductDetailDto;
import com.maciejjt.posinventory.model.enums.ProductLabel;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class ApiProductDto {
    private Long id;
    private String name;
    private Long UPC;
    private Set<ProductDetailDto> productDetails;
    private DiscountDto discount;
    private Set<ProductLabel> label;
    private String description;
    private String descriptionShort;
}
