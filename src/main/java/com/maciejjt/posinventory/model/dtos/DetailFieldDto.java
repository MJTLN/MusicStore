package com.maciejjt.posinventory.model.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetailFieldDto {
    private Long id;
    private String name;
    private String values;
}
