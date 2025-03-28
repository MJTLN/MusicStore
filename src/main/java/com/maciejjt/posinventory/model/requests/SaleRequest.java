package com.maciejjt.posinventory.model.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class SaleRequest {
    @NotNull(message = "Name cannot be null")
    @Size(min = 10, max = 255, message = "Sale name must be between 10 and 255 characters")
    private String name;
    @Size(min = 10, max = 320, message = "Sale description must be between 10 and 320 characters")
    private String description;
    private Set<Long> discountIds;
    @NotNull(message = "Sale type must be supplied")
    private boolean type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}