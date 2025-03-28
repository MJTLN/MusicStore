package com.maciejjt.posinventory.model.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PositionRequest {
    @NotNull(message = "Position id cannot be null")
    Long positionId;
    @NotNull(message = "Amount cannot be null")
    Integer amount;
}