package com.maciejjt.posinventory.model.dtos;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class CategoryDto {
    private Long id;
    private String name;
    private Long parentId;
    private Set<Long> childrenIds;
    private Set<Long> productIds;
}

