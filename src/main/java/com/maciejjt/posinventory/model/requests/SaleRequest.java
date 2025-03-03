package com.maciejjt.posinventory.model.requests;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class SaleRequest {
    private String name;
    private String description;
    private Set<Long> discountIds;
    private Boolean type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
