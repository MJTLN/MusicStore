package com.maciejjt.posinventory.model.dtos;

import com.maciejjt.posinventory.model.enums.ProductLabel;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class ProductDto implements IProductDto {
     private Long id;
     private String name;
     private Integer UPC;
     private BigDecimal price;
     private LocalDateTime createdAt;
     private LocalDateTime updatedAt;
     private Set<ProductDetailDto> productDetails;
     private Set<InventoryDto> inventories;
     private DiscountDto discount;
     private Set<ProductLabel> label;
     private Integer totalQuantity;
     private String description;
     private String descriptionShort;
}