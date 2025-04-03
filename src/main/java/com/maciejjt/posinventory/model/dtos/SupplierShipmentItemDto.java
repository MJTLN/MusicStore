package com.maciejjt.posinventory.model.dtos;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SupplierShipmentItemDto {
    IProductDto productDto;
    Integer quantity;
}
