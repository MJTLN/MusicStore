package com.maciejjt.posinventory.admin;

import com.maciejjt.posinventory.model.dtos.StorageDto;
import com.maciejjt.posinventory.model.dtos.StorageMovementDto;
import com.maciejjt.posinventory.model.enums.ShipmentStatus;
import com.maciejjt.posinventory.model.requests.PositionRequest;
import com.maciejjt.posinventory.model.requests.StorageMovementRequest;
import com.maciejjt.posinventory.service.InventoryService;
import com.maciejjt.posinventory.service.StorageMovementService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@Data
@RequestMapping("/admin/movement")
public class AdminMovementController {

    private final StorageMovementService storageMovementService;

    @GetMapping("/{movementId}")
    public ResponseEntity<StorageMovementDto> getMovementById(@PathVariable Long movementId) {
        return ResponseEntity.ok(storageMovementService.getMovementById(movementId));
    }
    @PostMapping("")
    public ResponseEntity<StorageDto> moveStorage(@RequestBody StorageMovementRequest storageMovementRequest) {
        Long id = storageMovementService.moveStorage(storageMovementRequest).getId();
        URI location = URI.create("/admin/movement/" + id);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{storageId}/out")
    public ResponseEntity<List<StorageMovementDto>> getOutgoingMovementsByStorage(@PathVariable Long storageId) {
        return ResponseEntity.ok(storageMovementService.getOutgoingMovementsBtnStorage(storageId));
    }

    @GetMapping("/{storageId}/in")
    public ResponseEntity<List<StorageMovementDto>> getIncomingMovementsByStorage(@PathVariable Long storageId) {
        return ResponseEntity.ok(storageMovementService.getIncomingMovementsBtnStorage(storageId));
    }

    @PostMapping("/{movementId}/finalize")
    public ResponseEntity<StorageDto> finalizeMovement(@PathVariable Long movementId, @RequestBody Set<PositionRequest> positions) {
        return ResponseEntity.ok(storageMovementService.finalizeMovement(movementId, positions));
    }

    @PostMapping("/{movementId}/accept")
    public ResponseEntity<StorageDto> acceptMovement(@PathVariable Long movementId) {
        return ResponseEntity.ok(storageMovementService.acceptMovement(movementId));
    }

    @PostMapping("/{movementId}/send")
    public ResponseEntity<Void> sendMovement(@PathVariable Long movementId, @RequestBody Set<PositionRequest> positions) {
        storageMovementService.sendMovement(movementId, positions);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{movementId}")
    public ResponseEntity<Void> editMovement(@PathVariable Long movementId, @RequestBody StorageMovementRequest storageMovementRequest) {
        storageMovementService.editMovement(movementId, storageMovementRequest);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{movementId}/shipping")
    public ResponseEntity<Void> updateMovementShipping(@PathVariable Long movementId, @RequestBody ShipmentStatus shipmentStatus) {
        storageMovementService.changeMovementShipping(movementId, shipmentStatus);
        return ResponseEntity.noContent().build();
    }
}
