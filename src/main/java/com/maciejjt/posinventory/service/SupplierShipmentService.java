package com.maciejjt.posinventory.service;

import com.maciejjt.posinventory.exceptions.BadStatusException;
import com.maciejjt.posinventory.exceptions.EntityNotFoundException;
import com.maciejjt.posinventory.model.*;
import com.maciejjt.posinventory.model.dtos.SupplierShipmentDto;
import com.maciejjt.posinventory.model.enums.ShipmentStatus;
import com.maciejjt.posinventory.model.requests.SupplierShipmentRequest;
import com.maciejjt.posinventory.repository.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@Data
public class SupplierShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final SupplierShipmentItemRepository supplierShipmentItemRepository;
    private final InventoryRepository inventoryRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final StorageRepository storageRepository;
    private final DTOservice dtoService;

    @Transactional
    public SupplierShipmentDto createShipment(SupplierShipmentRequest supplierShipmentRequest) {

        Supplier supplier = findSupplierById(supplierShipmentRequest.getSupplierId());

        Storage storage = findStorageById(supplierShipmentRequest.getStorageId());

        SupplierShipment supplierShipment = SupplierShipment.builder()
                .supplier(supplier)
                .createdAt(LocalDateTime.now())
                .arrivalTime(supplierShipmentRequest.getArrivalTime())
                .amount(supplierShipmentRequest.getAmount())
                .storage(storage)
                .status(ShipmentStatus.PLACED)
                .note(supplierShipmentRequest.getNote())
                .build();

        SupplierShipment savedShipment = shipmentRepository.save(supplierShipment);

        Set<SupplierShipmentItem> supplierShipmentItems = new HashSet<>();

        supplierShipmentRequest.getSupplierShipmentItemRequest().forEach(item -> {

            Product product = findProductById(item.getProductId());
            product.getInventories().forEach(inventoryLocation -> {
                if (inventoryLocation.getStorage().getId().equals(supplierShipmentRequest.getStorageId())){
                    inventoryLocation.getSupplierShipments().add(savedShipment);
                }
            });

            SupplierShipmentItem supplierShipmentItem = supplierShipmentItemRepository.save(
                    SupplierShipmentItem.builder()
                        .product(product)
                        .quantity(item.getQuantity())
                        .supplierShipment(supplierShipment)
                        .build());


            supplierShipmentItems.add(supplierShipmentItem);
        });

        savedShipment.setSupplierShipmentItems(supplierShipmentItems);

        return dtoService.buildShipmentWithListingsDto(shipmentRepository.save(supplierShipment));
    }

    public SupplierShipmentDto getShipmentWithListings(Long id) {

        SupplierShipment supplierShipment = findSupplierShipmentById(id);

        return dtoService.buildShipmentWithListingsDto(supplierShipment);
    }

    @Transactional
    public void finalizeShipment(Long id) {

        SupplierShipment shipment = findSupplierShipmentById(id);

        if (shipment.getStatus() != ShipmentStatus.COMPLETED) {

            shipment.setStatus(ShipmentStatus.COMPLETED);
            shipment.setArrivalTime(LocalDateTime.now());

            shipment.getSupplierShipmentItems().forEach(item -> {
                Product product = item.getProduct();

                Inventory inventory = product.getInventories().stream()
                        .filter(inv -> inv.getStorage().equals(shipment.getStorage()))
                        .findFirst()
                        .orElse(Inventory.builder()
                                .storage(shipment.getStorage())
                                .product(product)
                                .quantity(0)
                                .build());

                inventory.addQuantity(item.getQuantity());
                inventoryRepository.save(inventory);
            });

            shipmentRepository.save(shipment);
        } else  {
            throw new BadStatusException("Cannot finalize shipment marked as complete");
        }
    }

    @Transactional
    public SupplierShipmentDto updateShipment(Long id, SupplierShipmentRequest shipmentRequest) {
        SupplierShipment supplierShipment = findSupplierShipmentById(id);

        BeanUtils.copyProperties(shipmentRequest, supplierShipment, "supplierShipmentItemRequest");

        Supplier supplier = findSupplierById(shipmentRequest.getSupplierId());
        Storage storage = findStorageById(shipmentRequest.getStorageId());

        supplierShipment.setSupplier(supplier);
        supplierShipment.setStorage(storage);

        Set<SupplierShipmentItem> supplierShipmentItems = new HashSet<>();

        shipmentRequest.getSupplierShipmentItemRequest().forEach(item -> {

            Product product = findProductById(item.getProductId());

            SupplierShipmentItem supplierShipmentItem = supplierShipmentItemRepository.save(
                    SupplierShipmentItem.builder()
                            .product(product)
                            .quantity(item.getQuantity())
                            .supplierShipment(supplierShipment)
                            .build());


            supplierShipmentItems.add(supplierShipmentItem);
        });

        supplierShipment.setSupplierShipmentItems(supplierShipmentItems);

        return dtoService.buildShipmentWithListingsDto(supplierShipment);
    }

    public void deleteShipmentById(Long id) {
        shipmentRepository.deleteById(id);
    }

    public SupplierShipmentDto updateShipmentStatus(Long id, ShipmentStatus shipmentStatus) {
        SupplierShipment supplierShipment = findSupplierShipmentById(id);
        supplierShipment.setStatus(shipmentStatus);
        return dtoService.buildShipmentWithListingsDto(shipmentRepository.save(supplierShipment));
    }

    private SupplierShipment findSupplierShipmentById(Long id) {
        return shipmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Supplier shipment not found with id: " + id));
    }

    private Supplier findSupplierById(Long id) {
        return supplierRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + id));
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    private Storage findStorageById(Long id) {
        return storageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Storage not found with id: " + id));
    }

}
