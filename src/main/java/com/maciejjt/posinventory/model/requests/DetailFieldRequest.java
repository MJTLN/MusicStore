package com.maciejjt.posinventory.model.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class DetailFieldRequest {
    @NotNull(message = "Detail field name cannot be null")
    @Size(min = 3, max = 100, message = "Detail field name must be between 3 and 100 characters")
    String name;
    List<Long> categoryIds;
}
