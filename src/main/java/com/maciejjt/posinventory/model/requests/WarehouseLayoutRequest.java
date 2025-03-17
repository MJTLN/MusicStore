package com.maciejjt.posinventory.model.requests;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class WarehouseLayoutRequest {
    Map<String, Map<String, Map<String, List<String>>>> layout;
    Long storageId;
}
