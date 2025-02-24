package com.maciejjt.posinventory.model.requests;

import lombok.Data;

import java.util.List;

@Data
public class CategoryRequest {
    private String name;
    private Long parentId;
    private List<Long> childrenId;
}