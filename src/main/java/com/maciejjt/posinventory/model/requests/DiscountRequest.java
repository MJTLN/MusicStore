package com.maciejjt.posinventory.model.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DiscountRequest {
    @NotNull(message = "Discount name cannot be null")
    @Size(min = 3, max = 100 , message = "Discount name must be between 3 and 100 characters")
    private String name;
    @NotNull(message = "Must specify if discount is fixed value or percentage based")
    private boolean isFixedValue;
    @NotNull(message = "Amount cannot be null")
    private Integer amount;
    private LocalDateTime startDate;
    private Long saleId;
    private LocalDateTime endDate;
}