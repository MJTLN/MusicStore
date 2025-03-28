package com.maciejjt.posinventory.model.requests;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SupplierRequest {
    @NotNull(message = "Company name cannot be null")
    @Size(min = 10, max = 255, message = "Company name must be between 10 and 255 characters")
    private String companyName;
    @NotNull(message = "Company name cannot be null")
    @Size(min = 10, max = 350, message = "Official address must be between 10 and 350 characters")
    private String officialAddress;
    @NotNull(message = "Company name cannot be null")
    @Size(min = 10, max = 1000, message = "Contact info must be between 10 and 10000 characters")
    private String contactInfo;
}