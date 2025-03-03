package com.maciejjt.posinventory.model.dtos;

import com.maciejjt.posinventory.model.enums.ShipmentStatus;
import com.maciejjt.posinventory.model.enums.StorageMovementStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StorageMovementDto {
    Long id;
    Set<StorageMovementItemDto> movementItems;
    StorageBriefDto currentStorage;
    StorageBriefDto newStorage;
    String note;
    StorageMovementStatus status;
    ShipmentStatus shipmentStatus;
}
