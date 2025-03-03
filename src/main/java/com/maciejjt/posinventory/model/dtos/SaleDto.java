package com.maciejjt.posinventory.model.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class SaleDto {
    private Long id;
    private String name;
    private String description;
    private Set<DiscountDto> discounts;
    private Boolean type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
