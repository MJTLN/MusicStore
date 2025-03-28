package com.maciejjt.posinventory.model.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CategoryRequest {
    @NotNull(message = "Category name cannot be null")
    @Size(min = 3, max = 100 , message = "Category name must be between 3 and 100 characters")
    private String name;
    private Long parentId;
    private List<Long> childrenId;
}