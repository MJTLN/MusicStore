package com.maciejjt.posinventory.model.api.requests;
import lombok.Data;

@Data
public class PurchaseIssueRequest {
    public String description;
    public Long purchaseId;
}
