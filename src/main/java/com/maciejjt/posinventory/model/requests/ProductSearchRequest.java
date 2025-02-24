package com.maciejjt.posinventory.model.requests;

import com.maciejjt.posinventory.model.enums.ProductLabel;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Builder
public class ProductSearchRequest {
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Map<String,String> exactValue;
    private Map<String, List<String>> multipleValues;
    private Long categoryId;
    private Set<ProductLabel> labels;
}