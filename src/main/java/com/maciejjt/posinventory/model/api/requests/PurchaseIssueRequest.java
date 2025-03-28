package com.maciejjt.posinventory.model.api.requests;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PurchaseIssueRequest {
    @NotNull(message = "Product name cannot be null")
    @Size(min = 15, max = 1000, message = "Issue description must be between 15 and 1000 characters")
    public String description;
    @NotNull(message = "You must supply a purchase id")
    public Long purchaseId;
}
