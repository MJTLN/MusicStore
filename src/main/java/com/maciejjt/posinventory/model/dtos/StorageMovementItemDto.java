package com.maciejjt.posinventory.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StorageMovementItemDto {
    Long id;
    ProductListingDtoShort product;
    Integer quantity;
    boolean accepted;
}
