package com.maciejjt.posinventory.model.requests;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DiscountRequest {
    private String name;
    private boolean isFixedAmount;
    private Long amount;
    private LocalDateTime startDate;
    private Long saleId;
    private LocalDateTime endDate;
}


