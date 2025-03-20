package com.maciejjt.posinventory.model.requests;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CategoryRequest {
    private String name;
    private Long parentId;
    private List<Long> childrenId;
}