package com.maciejjt.posinventory.service;

import com.maciejjt.posinventory.exceptions.EntityNotFoundException;
import com.maciejjt.posinventory.exceptions.WarehouseConflictException;
import com.maciejjt.posinventory.model.*;
import com.maciejjt.posinventory.model.dtos.*;
import com.maciejjt.posinventory.model.requests.*;
import com.maciejjt.posinventory.repository.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Data
public class InventoryService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final StorageRepository storageRepository;
    private final SupplierRepository supplierRepository;
    private final ShipmentRepository shipmentRepository;
    private final SupplierShipmentItemRepository supplierShipmentItemRepository;
    private final DTOservice dtOservice;
    private final StorageMovementRepository storageMovementRepository;
    private final StorageMovementItemRepository storageMovementItemRepository;
    public final StorageService storageService;

    @Transactional
    public Inventory createInventoryForProduct(Long productId, InventoryRequest inventoryRequest) {
        Product product = findProductById(productId);
        Storage storage = findStorageById(inventoryRequest.getStorageId());
        Set<Inventory> inventories = product.getInventories();

        if (storage.getInventories().stream()
                .anyMatch(inventory -> inventory.getProduct().getId().equals(productId))
        ) {
            throw new WarehouseConflictException("There is already a inventory entity for this product in the specified storage");
        }

        Inventory inventory = Inventory.builder()
                    .quantity(inventoryRequest.getQuantity())
                    .storage(storage)
                    .product(product)
                    .build();

        inventoryRepository.save(inventory);

        inventories.add(inventory);
        product.setInventories(inventories);

        productRepository.save(product);

        return inventory;
    }

    @Transactional
    public void createInventoryForProduct(Product product, Storage storage, Integer quantity) {
        Inventory inventory = Inventory.builder()
                .quantity(quantity)
                .storage(storage)
                .product(product)
                .build();

        inventoryRepository.save(inventory);

        Set<Inventory> inventories = product.getInventories();
        product.setInventories(inventories);
        productRepository.save(product);
    }

    public InventoryDto getInventoryById(Long inventoryId) {
            return dtOservice.buildInventoryLocationDto(findInventoryById(inventoryId));
    }


    @Transactional
    public InventoryDto updateInventory(Long inventoryId, InventoryRequest inventoryRequest) {
                Inventory inventory =  findInventoryById(inventoryId);
                if(!inventory.getStorage().getId().equals(inventoryRequest.getStorageId())) {
                    Storage storage = findStorageById(inventoryRequest.getStorageId());
                    inventory.setStorage(storage);
                }
                inventory.setQuantity(inventoryRequest.getQuantity());
                return dtOservice.buildInventoryLocationDto(inventoryRepository.save(inventory));

    }

    public void deleteInventoryById(Long id) {
        inventoryRepository.deleteById(id);
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    private Storage findStorageById(Long id) {
        return storageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Storage not found with id: " + id));
    }

    private Inventory findInventoryById(Long id) {
        return inventoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Inventory not found with id: " + id));
    }
}