package com.maciejjt.posinventory.model.requests;

import lombok.Data;

import java.util.Map;

@Data
public class PurchaseCompletingRequest {
    Long purchaseId;
    Map<Long, PositionRequest> productStock;
}
