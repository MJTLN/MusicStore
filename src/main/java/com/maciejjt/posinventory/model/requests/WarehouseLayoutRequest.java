package com.maciejjt.posinventory.model.requests;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class WarehouseLayoutRequest {
    Map<String, Map<String, Map<String, List<String>>>> layout;
    Long storageId;
}
