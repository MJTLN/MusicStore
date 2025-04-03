package com.maciejjt.posinventory.admin;

import com.maciejjt.posinventory.model.dtos.SupplierShipmentDto;
import com.maciejjt.posinventory.model.enums.ShipmentStatus;
import com.maciejjt.posinventory.model.requests.SupplierShipmentRequest;
import com.maciejjt.posinventory.service.SupplierShipmentService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Data
@RequestMapping("/admin/shipment")
public class AdminShipmentController {

    private final SupplierShipmentService supplierShipmentService;

    @PostMapping()
    public ResponseEntity<SupplierShipmentDto> createShipment(@RequestBody SupplierShipmentRequest supplierShipmentRequest) {
        Long id = supplierShipmentService.createShipment(supplierShipmentRequest).getId();
        URI location = URI.create("/admin/shipment/" + id);
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{shipmentId}")
    public ResponseEntity<SupplierShipmentDto> updateShipment(@PathVariable Long shipmentId, @RequestBody SupplierShipmentRequest shipmentRequest) {
        return ResponseEntity.ok(supplierShipmentService.updateShipment(shipmentId,shipmentRequest));
    }

    @PatchMapping("/{shipmentId}")
    public ResponseEntity<SupplierShipmentDto> updateShipmentStatus(@PathVariable Long shipmentId, @RequestParam ShipmentStatus shipmentStatus) {
        return ResponseEntity.ok(supplierShipmentService.updateShipmentStatus(shipmentId, shipmentStatus));
    }

    @GetMapping("/{shipmentId}")
    public SupplierShipmentDto getShipmentWithProductListings(@PathVariable Long shipmentId) {
        return supplierShipmentService.getShipmentWithListings(shipmentId);
    }

    @PostMapping("/{shipmentId}/finalize")
    public ResponseEntity<Void> finalizeShipment(@PathVariable Long shipmentId) {
        supplierShipmentService.finalizeShipment(shipmentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{shipmentId}")
    public ResponseEntity<Void> deleteShipmentById(@PathVariable Long shipmentId) {
        supplierShipmentService.deleteShipmentById(shipmentId);
        return ResponseEntity.noContent().build();
    }
}
