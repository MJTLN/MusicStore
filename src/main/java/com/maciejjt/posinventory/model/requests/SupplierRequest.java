package com.maciejjt.posinventory.model.requests;
import lombok.Data;

@Data
public class SupplierRequest {
    private String companyName;
    private String officialAddress;
    private String contactInfo;
}
