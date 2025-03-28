package com.maciejjt.posinventory.admin;

import com.maciejjt.posinventory.model.dtos.WarehouseLayoutDto;
import com.maciejjt.posinventory.model.dtos.WarehouseLayoutDtoDetailed;
import com.maciejjt.posinventory.model.requests.WarehouseLayoutRequest;
import com.maciejjt.posinventory.service.InventoryService;
import com.maciejjt.posinventory.service.StorageService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Data
@RequestMapping("/admin/warehouse-layout")
public class AdminWarehouseLayoutController {

    private final StorageService storageService;

    @GetMapping("/{storageId}/layout")
    public ResponseEntity<WarehouseLayoutDto> getWarehouseLayoutForStorage(@PathVariable Long storageId) {
        return ResponseEntity.ok(storageService.getWarehouseLayoutForStorage(storageId));
    }

    @GetMapping("/{storageId}/layout-detailed")
    public ResponseEntity<WarehouseLayoutDtoDetailed> getWarehouseLayoutDetailedForStorage(@PathVariable Long storageId) {
        return ResponseEntity.ok(storageService.getWarehouseLayoutDetailedForStorage(storageId));
    }

    @PostMapping
    public ResponseEntity<Void> createWarehouseLayout(@RequestBody WarehouseLayoutRequest request) {
        Long id = storageService.createWarehouseLayout(request).getId();
        URI location = URI.create("/admin/warehouse-layout/" + id);
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{positionId}")
    public ResponseEntity<Void> putProductOnPosition(@PathVariable Long positionId, @RequestParam Long productId, @RequestParam Integer quantity) {
        storageService.putProductOnPosition(positionId, productId, quantity);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(("/{positionId}"))
    public ResponseEntity<Void> updatePositionQuantity(@PathVariable Long positionId, @RequestParam boolean addOrSubtract, @RequestParam Integer quantity) {
        storageService.updatePositionProductQuantity(positionId, quantity, addOrSubtract);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(("/{positionId}"))
    public ResponseEntity<Void> movePosition(@PathVariable Long positionId, @RequestParam Long newPositionId) {
        storageService.movePosition(positionId, newPositionId);
        return ResponseEntity.noContent().build();
    }
}