package com.maciejjt.posinventory.admin;

import com.maciejjt.posinventory.model.Supplier;
import com.maciejjt.posinventory.model.dtos.SupplierDto;
import com.maciejjt.posinventory.model.dtos.SupplierDtoWithShipments;
import com.maciejjt.posinventory.model.requests.SupplierRequest;
import com.maciejjt.posinventory.service.InventoryService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Data
@RequestMapping("/admin/supplier")
public class AdminSupplierController {

    private final InventoryService inventoryService;

    @PostMapping()
    public ResponseEntity<Supplier> createSupplier(@RequestBody SupplierRequest request) {
        return ResponseEntity.ok(inventoryService.createSupplier(request));
    }

    @PutMapping("/{supplierId}")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable Long supplierId, @RequestBody SupplierRequest supplierRequest) {
        return ResponseEntity.ok(inventoryService.updateSupplier(supplierId,supplierRequest));
    }

    @GetMapping("/{supplierId}")
    public ResponseEntity<SupplierDto> findSupplierById(@PathVariable Long supplierId) {
        return ResponseEntity.ok(inventoryService.getSupplierById(supplierId));
    }

    @GetMapping("/{supplierId}/with-shipments")
    public ResponseEntity<SupplierDtoWithShipments> findSupplierWithShipmentsById(@PathVariable Long supplierId) {
        return ResponseEntity.ok(inventoryService.findSupplierWithShipmentsById(supplierId));
    }

    @DeleteMapping("/{supplierId}")
    public ResponseEntity<Void> deleteSupplierById(@PathVariable Long supplierId) {
        inventoryService.deleteSupplierById(supplierId);
        return ResponseEntity.ok().build();
    }
}
