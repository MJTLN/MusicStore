package com.maciejjt.posinventory.admin;

import com.maciejjt.posinventory.model.dtos.StorageBriefDto;
import com.maciejjt.posinventory.model.dtos.StorageDto;
import com.maciejjt.posinventory.model.requests.StorageRequest;
import com.maciejjt.posinventory.service.StorageService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Data
@RequestMapping("/admin/storage")
public class AdminStorageController {

    private final StorageService storageService;

    @PostMapping()
    public ResponseEntity<Void> createStorage(StorageRequest storageRequest) {
        Long id = storageService.createStorage(storageRequest).getId();
        URI location = URI.create("/admin/storage/" + id);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{storageId}")
    public ResponseEntity<StorageDto> getStorageById(@PathVariable Long storageId) {
        return ResponseEntity.ok(storageService.getStorageById(storageId));
    }

    @DeleteMapping("/{storageId}")
    public ResponseEntity<Void> deleteStorageById(@PathVariable Long storageId){
        storageService.deleteStorageById(storageId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{storageId}/brief")
    public ResponseEntity<StorageBriefDto> getStorageBriefById(@PathVariable Long storageId) {
        return ResponseEntity.ok(storageService.getStorageBriefById(storageId));
    }

    @GetMapping("/{storageId}/products")
    public ResponseEntity<StorageDto> getStorageWithProducts(@PathVariable Long storageId) {
        return ResponseEntity.ok(storageService.getStorageWithProducts(storageId));
    }
}