package com.maciejjt.posinventory.admin;

import com.maciejjt.posinventory.model.Supplier;
import com.maciejjt.posinventory.model.dtos.SupplierDto;
import com.maciejjt.posinventory.model.dtos.SupplierDtoWithShipments;
import com.maciejjt.posinventory.model.requests.SupplierRequest;
import com.maciejjt.posinventory.service.SupplierService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Data
@RequestMapping("/admin/supplier")
public class AdminSupplierController {

    private final SupplierService supplierService;

    @PostMapping()
    public ResponseEntity<Supplier> createSupplier(@RequestBody SupplierRequest request) {
        Long id = supplierService.createSupplier(request).getId();
        URI location = URI.create("/admin/supplier/" + id);
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{supplierId}")
    public ResponseEntity<SupplierDto> updateSupplier(@PathVariable Long supplierId, @RequestBody SupplierRequest supplierRequest) {
        return ResponseEntity.ok(supplierService.updateSupplier(supplierId,supplierRequest));
    }

    @GetMapping("/{supplierId}")
    public ResponseEntity<SupplierDto> findSupplierById(@PathVariable Long supplierId) {
        return ResponseEntity.ok(supplierService.getSupplierById(supplierId));
    }

    @GetMapping("/{supplierId}/with-shipments")
    public ResponseEntity<SupplierDtoWithShipments> findSupplierWithShipmentsById(@PathVariable Long supplierId) {
        return ResponseEntity.ok(supplierService.findSupplierWithShipmentsById(supplierId));
    }

    @DeleteMapping("/{supplierId}")
    public ResponseEntity<Void> deleteSupplierById(@PathVariable Long supplierId) {
        supplierService.deleteSupplierById(supplierId);
        return ResponseEntity.noContent().build();
    }
}
