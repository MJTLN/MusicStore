package com.maciejjt.posinventory.admin;

import com.maciejjt.posinventory.model.*;
import com.maciejjt.posinventory.model.dtos.*;
import com.maciejjt.posinventory.model.enums.ShipmentStatus;
import com.maciejjt.posinventory.model.requests.*;
import com.maciejjt.posinventory.service.InventoryService;
import com.maciejjt.posinventory.service.ProductService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@Data
@RequestMapping("/admin")
public class   AdminInventoryController {

    private final InventoryService inventoryService;
    private final ProductService productService;

    @PostMapping("/inventory/{productId}")
    public ResponseEntity<Void> createInventoryForProduct(@PathVariable Long productId, @RequestBody InventoryRequest inventoryRequest) {
        inventoryService.createInventoryForProduct(productId, inventoryRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/inventory/{inventoryId}")
    public ResponseEntity<InventoryDto> updateInventory(@PathVariable Long inventoryId, @RequestBody InventoryRequest inventoryRequest) {
        return ResponseEntity.ok(inventoryService.updateInventoryLocation(inventoryId, inventoryRequest));
    }

    @GetMapping("/inventory/{inventoryId}")
    public ResponseEntity<InventoryDto> getInventoryById(@PathVariable Long inventoryId) {
        return ResponseEntity.ok(inventoryService.getInventoryById(inventoryId));
    }

    @DeleteMapping("/inventory/{inventoryId}")
    public ResponseEntity<Void> deleteInventoryById(@PathVariable Long inventoryId) {
        inventoryService.deleteInventoryLocationById(inventoryId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/inventory/{inventoryId}/positions")
    public ResponseEntity<Set<PositionDto>> getPositionsForInventory(@PathVariable Long inventoryId) {
        return ResponseEntity.ok(inventoryService.getPositionsForInventory(inventoryId));
    }


}