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

@RestController
@Data
@RequestMapping("/admin")
public class  AdminInventoryController {

    private final InventoryService inventoryService;
    private final ProductService productService;

    //INVENTORY LOCATIONS

    @PostMapping("/inventory/{productId}")
    public ResponseEntity<Void> createInventoryForProduct(@PathVariable Long productId, @RequestBody InventoryRequest inventoryRequest) {
        inventoryService.createInventoryForProduct(productId, inventoryRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/inventory/{inventoryId}")
    public ResponseEntity<InventoryLocationDto> updateInventory(@PathVariable Long inventoryId, @RequestBody InventoryRequest inventoryRequest) {
        return ResponseEntity.ok(inventoryService.updateInventoryLocation(inventoryId, inventoryRequest));
    }

    @GetMapping("/inventory/{inventoryId}")
    public ResponseEntity<InventoryLocationDto> getInventoryById(@PathVariable Long inventoryId) {
        return ResponseEntity.ok(inventoryService.getInventoryById(inventoryId));
    }

    @DeleteMapping("/inventory/{inventoryId}")
    public ResponseEntity<Void> deleteInventoryById(@PathVariable Long inventoryId) {
        inventoryService.deleteInventoryLocationById(inventoryId);
        return ResponseEntity.ok().build();
    }

    //SHIPMENTS

    @PostMapping("/shipment")
    public ResponseEntity<SupplierShipmentDto> createShipment(@RequestBody SupplierShipmentRequest supplierShipmentRequest) {
        return ResponseEntity.ok(inventoryService.createShipment(supplierShipmentRequest));
    }

    @PutMapping("/shipment/{shipmentId}")
    public ResponseEntity<SupplierShipmentDto> updateShipment(@PathVariable Long shipmentId, @RequestBody SupplierShipmentRequest shipmentRequest) {
        return ResponseEntity.ok(inventoryService.updateShipment(shipmentId,shipmentRequest));
    }

    @GetMapping("/shipment/{shipmentId}")
    public SupplierShipmentDto getShipmentWithProductListings(@PathVariable Long shipmentId) {
        return inventoryService.getShipmentWithListings(shipmentId);
    }

    @PostMapping("/shipment/{shipmentId}/finalize")
    public ResponseEntity<Void> finalizeShipment(@PathVariable Long shipmentId) {
        inventoryService.finalizeShipment(shipmentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/shipment/{shipmentId}")
    public ResponseEntity<Void> deleteShipmentById(@PathVariable Long shipmentId) {
        inventoryService.deleteShipmentById(shipmentId);
        return ResponseEntity.ok().build();
    }

    //SUPPLIERS

    @PostMapping("/supplier")
    public ResponseEntity<Supplier> createSupplier(@RequestBody SupplierRequest request) {
        return ResponseEntity.ok(inventoryService.createSupplier(request));
    }

    @PutMapping("/supplier/{supplierId}")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable Long supplierId, @RequestBody SupplierRequest supplierRequest) {
        return ResponseEntity.ok(inventoryService.updateSupplier(supplierId,supplierRequest));
    }

    @GetMapping("supplier/{supplierId}/")
    public ResponseEntity<SupplierDto> findSupplierById(@PathVariable Long supplierId) {
        return ResponseEntity.ok(inventoryService.getSupplierById(supplierId));
    }

    @GetMapping("supplier/{supplierId}/with-shipments")
    public ResponseEntity<SupplierDtoWithShipments> findSupplierWithShipmentsById(@PathVariable Long supplierId) {
        return ResponseEntity.ok(inventoryService.findSupplierWithShipmentsById(supplierId));
    }

    @DeleteMapping("supplier/{supplierId}")
    public ResponseEntity<Void> deleteSupplierById(@PathVariable Long supplierId) {
        inventoryService.deleteSupplierById(supplierId);
        return ResponseEntity.ok().build();
    }

    //STORAGE

    @PostMapping("/storage")
    public ResponseEntity<StorageDto> createStorage(StorageRequest storageRequest) {
        return ResponseEntity.ok(inventoryService.createStorage(storageRequest));
    }

    @GetMapping("/storage/{storageId}")
    public ResponseEntity<StorageDto> getStorageById(@PathVariable Long storageId) {
        return ResponseEntity.ok(inventoryService.getStorageById(storageId));
    }

    @DeleteMapping("storage/{storageId}")
    public ResponseEntity<Void> deleteStorageById(@PathVariable Long storageId){
        inventoryService.deleteStorageById(storageId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/storage/{storageId}/brief")
    public ResponseEntity<StorageBriefDto> getStorageBriefById(@PathVariable Long storageId) {
        return ResponseEntity.ok(inventoryService.getStorageBriefById(storageId));
    }

    @GetMapping("/storage/{storageId}/products")
    public ResponseEntity<StorageDto> getStorageWithProducts(@PathVariable Long storageId) {
        return ResponseEntity.ok(inventoryService.getStorageWithProducts(storageId));
    }

    //INVENTORY MOVEMENTS

    @GetMapping("movement/{movementId}")
    public ResponseEntity<StorageMovementDto> getMovementById(@PathVariable Long movementId) {
        return ResponseEntity.ok(inventoryService.getMovementById(movementId));
    }

    @GetMapping("movement/{storageId}/out")
    public ResponseEntity<List<StorageMovementDto>> getOutgoingMovementsByStorage(@PathVariable Long storageId) {
        return ResponseEntity.ok(inventoryService.getOutgoingMovementsBtnStorage(storageId));
    }

    @GetMapping("movement/{storageId}/in")
    public ResponseEntity<List<StorageMovementDto>> getIncomingMovementsByStorage(@PathVariable Long storageId) {
        return ResponseEntity.ok(inventoryService.getIncomingMovementsBtnStorage(storageId));
    }

    @PostMapping("/movement")
    public ResponseEntity<StorageDto> moveStorage(@RequestBody StorageMovementRequest storageMovementRequest) {
        return ResponseEntity.ok(inventoryService.moveStorage(storageMovementRequest));
    }

    @PostMapping("/movement/{movementId}/finalize")
    public ResponseEntity<StorageDto> finalizeMovement(@PathVariable Long movementId) {
        return ResponseEntity.ok(inventoryService.finalizeMovement(movementId));
    }

    @PostMapping("/movement/{movementId}/accept")
    public ResponseEntity<StorageDto> acceptMovement(@PathVariable Long movementId) {
        return ResponseEntity.ok(inventoryService.acceptMovement(movementId));
    }

    @PostMapping("/movement/{movementId}/send")
    public ResponseEntity<Void> sendMovement(@PathVariable Long movementId) {
        inventoryService.sendMovement(movementId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/movement/{movementId}")
    public ResponseEntity<Void> editMovement(@PathVariable Long movementId, @RequestBody StorageMovementRequest storageMovementRequest) {
        inventoryService.editMovement(movementId, storageMovementRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/movement/{movementId}/shipping")
    public ResponseEntity<Void> updateMovementShipping(@PathVariable Long movementId, @RequestBody ShipmentStatus shipmentStatus) {
        inventoryService.changeMovementShipping(movementId, shipmentStatus);
        return ResponseEntity.ok().build();
    }

    //WAREHOUSE LOCATIONS
    @PostMapping("/warehouseLocation/{inventoryLocationId}")
    public ResponseEntity<InventoryLocationDto> addWarehouseLocation(@PathVariable Long inventoryLocationId, @RequestBody WarehouseLocationRequest warehouseLocationRequest) {
        return ResponseEntity.ok(inventoryService.addWarehouseLocation(inventoryLocationId, warehouseLocationRequest));
    }
}