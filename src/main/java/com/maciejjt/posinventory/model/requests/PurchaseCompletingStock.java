package com.maciejjt.posinventory.model.requests;

import lombok.Data;

@Data
public class PurchaseCompletingStock {
    Long warehouseLocationId;
    Integer amount;
}
