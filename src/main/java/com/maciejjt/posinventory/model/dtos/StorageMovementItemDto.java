package com.maciejjt.posinventory.model.dtos;

import com.maciejjt.posinventory.model.Product;
import com.maciejjt.posinventory.model.StorageMovement;
import jakarta.persistence.*;
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
    ProductListingDto product;
    Integer quantity;
    boolean accepted;
}
