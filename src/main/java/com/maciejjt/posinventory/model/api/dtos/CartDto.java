package com.maciejjt.posinventory.model.api.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class  CartDto {
    Long id;
    List<CartDtoItem> products;
}
