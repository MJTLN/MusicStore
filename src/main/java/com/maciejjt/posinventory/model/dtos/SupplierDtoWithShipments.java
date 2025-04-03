package com.maciejjt.posinventory.model.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class SupplierDtoWithShipments {
    private Long id;
    private String companyName;
    private String officialAddress;
    private String contactInfo;
    private Set<SupplierShipmentDtoNoSupplier> supplierShipments;
}
