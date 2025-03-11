package com.maciejjt.posinventory.model.requests;

import lombok.Data;

@Data
public class PositionRequest {
    Long positionId;
    Integer amount;
}
