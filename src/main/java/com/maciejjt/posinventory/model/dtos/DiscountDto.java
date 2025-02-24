package com.maciejjt.posinventory.model.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DiscountDto {
    private Long id;
    private boolean isFixedAmount;
    private Long amount;
    private String name;
    private Long saleId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Long productId;
}
