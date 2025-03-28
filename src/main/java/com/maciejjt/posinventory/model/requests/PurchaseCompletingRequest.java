package com.maciejjt.posinventory.model.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class PurchaseCompletingRequest {
    @NotNull(message = "Purchase id must not be null")
    Long purchaseId;
    @NotEmpty(message = "Must specify positions and quantities")
    Map<Long, PositionRequest> productStock;
}
