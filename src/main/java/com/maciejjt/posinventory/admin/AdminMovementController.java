package com.maciejjt.posinventory.admin;

import com.maciejjt.posinventory.model.dtos.StorageDto;
import com.maciejjt.posinventory.model.dtos.StorageMovementDto;
import com.maciejjt.posinventory.model.enums.ShipmentStatus;
import com.maciejjt.posinventory.model.requests.PositionRequest;
import com.maciejjt.posinventory.model.requests.StorageMovementRequest;
import com.maciejjt.posinventory.service.InventoryService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@Data
@RequestMapping("/admin/movement")
public class AdminMovementController {

    private final InventoryService inventoryService;

    @GetMapping("/{movementId}")
    public ResponseEntity<StorageMovementDto> getMovementById(@PathVariable Long movementId) {
        return ResponseEntity.ok(inventoryService.getMovementById(movementId));
    }
    @PostMapping("")
    public ResponseEntity<StorageDto> moveStorage(@RequestBody StorageMovementRequest storageMovementRequest) {
        return ResponseEntity.ok(inventoryService.moveStorage(storageMovementRequest));
    }

    @GetMapping("/{storageId}/out")
    public ResponseEntity<List<StorageMovementDto>> getOutgoingMovementsByStorage(@PathVariable Long storageId) {
        return ResponseEntity.ok(inventoryService.getOutgoingMovementsBtnStorage(storageId));
    }

    @GetMapping("/{storageId}/in")
    public ResponseEntity<List<StorageMovementDto>> getIncomingMovementsByStorage(@PathVariable Long storageId) {
        return ResponseEntity.ok(inventoryService.getIncomingMovementsBtnStorage(storageId));
    }

    @PostMapping("/{movementId}/finalize")
    public ResponseEntity<StorageDto> finalizeMovement(@PathVariable Long movementId, @RequestBody Set<PositionRequest> positions) {
        return ResponseEntity.ok(inventoryService.finalizeMovement(movementId, positions));
    }

    @PostMapping("/{movementId}/accept")
    public ResponseEntity<StorageDto> acceptMovement(@PathVariable Long movementId) {
        return ResponseEntity.ok(inventoryService.acceptMovement(movementId));
    }

    @PostMapping("/{movementId}/send")
    public ResponseEntity<Void> sendMovement(@PathVariable Long movementId, @RequestBody Set<PositionRequest> positions) {
        inventoryService.sendMovement(movementId, positions);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{movementId}")
    public ResponseEntity<Void> editMovement(@PathVariable Long movementId, @RequestBody StorageMovementRequest storageMovementRequest) {
        inventoryService.editMovement(movementId, storageMovementRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{movementId}/shipping")
    public ResponseEntity<Void> updateMovementShipping(@PathVariable Long movementId, @RequestBody ShipmentStatus shipmentStatus) {
        inventoryService.changeMovementShipping(movementId, shipmentStatus);
        return ResponseEntity.ok().build();
    }
}
