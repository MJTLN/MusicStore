package com.maciejjt.posinventory.admin;

import com.maciejjt.posinventory.model.dtos.StorageBriefDto;
import com.maciejjt.posinventory.model.dtos.StorageDto;
import com.maciejjt.posinventory.model.dtos.WarehouseLayoutDto;
import com.maciejjt.posinventory.model.dtos.WarehouseLayoutDtoDetailed;
import com.maciejjt.posinventory.model.requests.StorageRequest;
import com.maciejjt.posinventory.service.InventoryService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Data
@RequestMapping("/admin/storage")
public class AdminStorageController {

    private final InventoryService inventoryService;

    @PostMapping()
    public ResponseEntity<StorageDto> createStorage(StorageRequest storageRequest) {
        return ResponseEntity.ok(inventoryService.createStorage(storageRequest));
    }

    @GetMapping("/{storageId}")
    public ResponseEntity<StorageDto> getStorageById(@PathVariable Long storageId) {
        return ResponseEntity.ok(inventoryService.getStorageById(storageId));
    }

    @DeleteMapping("/{storageId}")
    public ResponseEntity<Void> deleteStorageById(@PathVariable Long storageId){
        inventoryService.deleteStorageById(storageId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{storageId}/brief")
    public ResponseEntity<StorageBriefDto> getStorageBriefById(@PathVariable Long storageId) {
        return ResponseEntity.ok(inventoryService.getStorageBriefById(storageId));
    }

    @GetMapping("/{storageId}/products")
    public ResponseEntity<StorageDto> getStorageWithProducts(@PathVariable Long storageId) {
        return ResponseEntity.ok(inventoryService.getStorageWithProducts(storageId));
    }

}
