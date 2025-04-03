package com.maciejjt.posinventory.admin;

import com.maciejjt.posinventory.model.dtos.*;
import com.maciejjt.posinventory.model.requests.*;
import com.maciejjt.posinventory.service.InventoryService;
import com.maciejjt.posinventory.service.ProductService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Set;

@RestController
@Data
@RequestMapping("/admin/inventory")
public class   AdminInventoryController {

    private final InventoryService inventoryService;
    private final ProductService productService;

    @PostMapping("/{productId}")
    public ResponseEntity<Void> createInventoryForProduct(@PathVariable Long productId, @RequestBody InventoryRequest inventoryRequest) {
        Long id = inventoryService.createInventoryForProduct(productId, inventoryRequest).getId();
        URI location = URI.create("/admin/inventory" + id);
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{inventoryId}")
    public ResponseEntity<InventoryDto> updateInventory(@PathVariable Long inventoryId, @RequestBody InventoryRequest inventoryRequest) {
        return ResponseEntity.ok(inventoryService.updateInventory(inventoryId, inventoryRequest));
    }

    @GetMapping("/{inventoryId}")
    public ResponseEntity<InventoryDto> getInventoryById(@PathVariable Long inventoryId) {
        return ResponseEntity.ok(inventoryService.getInventoryById(inventoryId));
    }

    @DeleteMapping("/{inventoryId}")
    public ResponseEntity<Void> deleteInventoryById(@PathVariable Long inventoryId) {
        inventoryService.deleteInventoryById(inventoryId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{inventoryId}/positions")
    public ResponseEntity<Set<PositionDto>> getPositionsForInventory(@PathVariable Long inventoryId) {
        return ResponseEntity.ok(inventoryService.getPositionsForInventory(inventoryId));
    }
}