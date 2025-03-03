package com.maciejjt.posinventory.model.requests;

import com.maciejjt.posinventory.model.enums.ProductLabel;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Set;


@Data
@Builder
public class ProductRequest {
    private String name;
    private Long UPC;
    private Map<Long,String> productDetails;
    private Set<Long> categoryIds;
    private Set<ProductLabel> labels;
    private String description;
    private String descriptionShort;
    private Integer price;
}