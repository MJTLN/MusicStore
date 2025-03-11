package com.maciejjt.posinventory.admin;

import com.maciejjt.posinventory.model.requests.WarehouseLayoutRequest;
import com.maciejjt.posinventory.service.InventoryService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Data
@RequestMapping("/admin/warehouse-layout")
public class AdminWarehouseLayoutController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<Void> createWarehouseLayout(@RequestBody WarehouseLayoutRequest request) {
        inventoryService.createWarehouseLayout(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
