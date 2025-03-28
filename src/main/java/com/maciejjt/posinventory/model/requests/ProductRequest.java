package com.maciejjt.posinventory.model.requests;

import com.maciejjt.posinventory.model.enums.ProductLabel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Set;


@Data
@Builder
public class ProductRequest {
    @NotNull(message = "Name cannot be null")
    @Size(min = 10, max = 255, message = "Product name must be between 10 and 255 characters")
    private String name;
    @NotNull(message = "UPC cannot be null")
    @Size(min = 8, max = 8, message = "UPC must be 8 digits long")
    private Integer UPC;
    private Map<Long,String> productDetails;
    private Set<Long> categoryIds;
    private Set<ProductLabel> labels;
    @Size(min = 10, max = 1000, message = "Product description must be between 10 and 1000 characters")
    private String description;
    @Size(min = 10, max = 1000, message = "Product short description must be between 10 and 255 characters")
    private String descriptionShort;
    @NotNull(message = "Price cannot be null")
    private Integer price;
}
