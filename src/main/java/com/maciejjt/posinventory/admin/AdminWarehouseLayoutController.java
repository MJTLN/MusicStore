package com.maciejjt.posinventory.admin;

import com.maciejjt.posinventory.model.dtos.WarehouseLayoutDto;
import com.maciejjt.posinventory.model.dtos.WarehouseLayoutDtoDetailed;
import com.maciejjt.posinventory.model.requests.WarehouseLayoutRequest;
import com.maciejjt.posinventory.service.InventoryService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Data
@RequestMapping("/admin/warehouse-layout")
public class AdminWarehouseLayoutController {

    private final InventoryService inventoryService;

    @GetMapping("/{storageId}/layout")
    public ResponseEntity<WarehouseLayoutDto> getWarehouseLayoutForStorage(@PathVariable Long storageId) {
        return ResponseEntity.ok(inventoryService.getWarehouseLayoutForStorage(storageId));
    }

    @GetMapping("/{storageId}/layout-detailed")
    public ResponseEntity<WarehouseLayoutDtoDetailed> getWarehouseLayoutDetailedForStorage(@PathVariable Long storageId) {
        return ResponseEntity.ok(inventoryService.getWarehouseLayoutDetailedForStorage(storageId));
    }

    @PostMapping
    public ResponseEntity<Void> createWarehouseLayout(@RequestBody WarehouseLayoutRequest request) {
        inventoryService.createWarehouseLayout(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{positionId}")
    public ResponseEntity<Void> putProductOnPosition(@PathVariable Long positionId, @RequestParam Long productId, @RequestParam Integer quantity) {
        inventoryService.putProductOnPosition(positionId, productId, quantity);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(("/{positionId}"))
    public ResponseEntity<Void> updatePositionQuantity(@PathVariable Long positionId, @RequestParam boolean addOrSubtract, @RequestParam Integer quantity) {
        inventoryService.updatePositionProductQuantity(positionId, quantity, addOrSubtract);
        return ResponseEntity.ok().build();
    }

    @PutMapping(("/{positionId}"))
    public ResponseEntity<Void> movePosition(@PathVariable Long positionId, @RequestParam Long newPositionId) {
        inventoryService.movePosition(positionId, newPositionId);
        return ResponseEntity.ok().build();
    }
}