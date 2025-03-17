package com.maciejjt.posinventory.admin;

import com.maciejjt.posinventory.model.dtos.SupplierShipmentDto;
import com.maciejjt.posinventory.model.enums.ShipmentStatus;
import com.maciejjt.posinventory.model.requests.PositionRequest;
import com.maciejjt.posinventory.model.requests.SupplierShipmentRequest;
import com.maciejjt.posinventory.service.InventoryService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@Data
@RequestMapping("/admin/shipment")
public class AdminShipmentController {

    private final InventoryService inventoryService;

    @PostMapping()
    public ResponseEntity<SupplierShipmentDto> createShipment(@RequestBody SupplierShipmentRequest supplierShipmentRequest) {
        return ResponseEntity.ok(inventoryService.createShipment(supplierShipmentRequest));
    }

    @PutMapping("/{shipmentId}")
    public ResponseEntity<SupplierShipmentDto> updateShipment(@PathVariable Long shipmentId, @RequestBody SupplierShipmentRequest shipmentRequest) {
        return ResponseEntity.ok(inventoryService.updateShipment(shipmentId,shipmentRequest));
    }

    @PatchMapping("/{shipmentId}")
    public ResponseEntity<SupplierShipmentDto> updateShipmentStatus(@PathVariable Long shipmentId, @RequestParam ShipmentStatus shipmentStatus) {
        return ResponseEntity.ok(inventoryService.updateShipmentStatus(shipmentId, shipmentStatus));
    }

    @GetMapping("/{shipmentId}")
    public SupplierShipmentDto getShipmentWithProductListings(@PathVariable Long shipmentId) {
        return inventoryService.getShipmentWithListings(shipmentId);
    }

    @PostMapping("/{shipmentId}/finalize")
    public ResponseEntity<Void> finalizeShipment(@PathVariable Long shipmentId) {
        inventoryService.finalizeShipment(shipmentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{shipmentId}")
    public ResponseEntity<Void> deleteShipmentById(@PathVariable Long shipmentId) {
        inventoryService.deleteShipmentById(shipmentId);
        return ResponseEntity.ok().build();
    }
}
