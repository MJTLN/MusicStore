package com.maciejjt.posinventory.model.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class WarehouseLayoutRequest {
    @NotEmpty
    Map<String, Map<String, Map<String, List<String>>>> layout;
    @NotNull(message = "Storage id cannot be null")
    Long storageId;
}
