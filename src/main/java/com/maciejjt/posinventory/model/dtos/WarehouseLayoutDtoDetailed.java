package com.maciejjt.posinventory.model.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Builder
@Data
public class WarehouseLayoutDtoDetailed {
    Map<String, Map<String, Map<String, Map<String, List<PositionDto>>>>> layout;
}
