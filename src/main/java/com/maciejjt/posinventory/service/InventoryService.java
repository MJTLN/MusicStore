package com.maciejjt.posinventory.service;

import com.maciejjt.posinventory.exceptions.EntityNotFoundException;
import com.maciejjt.posinventory.exceptions.StorageNotEmptiedException;
import com.maciejjt.posinventory.model.*;
import com.maciejjt.posinventory.model.dtos.*;
import com.maciejjt.posinventory.model.enums.ShipmentStatus;
import com.maciejjt.posinventory.model.enums.StorageMovementStatus;
import com.maciejjt.posinventory.model.requests.*;
import com.maciejjt.posinventory.repository.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final WarehouseLocationRepository warehouseLocationRepository;
    private final StorageMovementRepository storageMovementRepository;
    private final StorageMovementItemRepository storageMovementItemRepository;


    @Transactional
    public void createInventoryForProduct(Long productId, InventoryRequest inventoryRequest) {
        Product product = findProductById(productId);
        Storage storage = findStorageById(inventoryRequest.getStorageId());
        Set<Inventory> inventories = product.getInventories();

        /* NAPRAW
        if (inventories.stream()
                        .anyMatch(location -> location.getProduct().getId().equals(productId))) {
            throw new RuntimeException("THERE IS ALREADY A INVENTORY FOR THIS PRODUCT IN THIS STORAGE");
        }*/

        Inventory inventory = Inventory.builder()
                    .quantity(inventoryRequest.getQuantity())
                    .storage(storage)
                    .product(product)
                    .build();

        inventoryRepository.save(inventory);

        inventories.add(inventory);
        product.setInventories(inventories);

        productRepository.save(product);
    }

    public InventoryLocationDto getInventoryById(Long inventoryId) {
            return dtOservice.buildInventoryLocationDto(findInventoryById(inventoryId));
    }


    @Transactional
    public InventoryLocationDto updateInventoryLocation(Long inventoryId, InventoryRequest inventoryRequest) {
                Inventory inventory =  findInventoryById(inventoryId);
                //! CZY TO OPYMALNE ? MOZE DWIE ODDZIELNE METODY DO EDYYCJI TEGO I TEGO?
                if(!inventory.getStorage().getId().equals(inventoryRequest.getStorageId())) {
                    Storage storage = findStorageById(inventoryRequest.getStorageId());
                    inventory.setStorage(storage);
                }
                inventory.setQuantity(inventoryRequest.getQuantity());
                return dtOservice.buildInventoryLocationDto(inventoryRepository.save(inventory));

    }

    public void deleteInventoryLocationById(Long id) {
        inventoryRepository.deleteById(id);
    }

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

        return dtOservice.buildShipmentWithListingsDto(shipmentRepository.save(supplierShipment));
    }

    public Supplier createSupplier(SupplierRequest request) {
        Supplier supplier = Supplier.builder()
                .companyName(request.getCompanyName())
                .contactInfo(request.getContactInfo())
                .officialAddress(request.getOfficialAddress())
                .build();

        return supplierRepository.save(supplier);
    }

    public SupplierShipmentDto getShipmentWithListings(Long id) {

        SupplierShipment supplierShipment = findSupplierShipmentById(id);

        return dtOservice.buildShipmentWithListingsDto(supplierShipment);
    }

    @Transactional
    public void finalizeShipment(Long id) {

        SupplierShipment shipment = findSupplierShipmentById(id);

        if (shipment.getStatus() != ShipmentStatus.COMPLETED) {

            shipment.setStatus(ShipmentStatus.COMPLETED);
            shipment.setArrivalTime(LocalDateTime.now());

            shipment.getSupplierShipmentItems().forEach(item -> {
                Product product = item.getProduct();
                product.getInventories().forEach(inventoryLocation -> {
                    if (inventoryLocation.getStorage() == shipment.getStorage()){
                        inventoryLocation.addQuantity(item.getQuantity());
                        inventoryRepository.save(inventoryLocation);
                    }
                });
            });

            shipmentRepository.save(shipment);

        }
    }

    public StorageDto createStorage(StorageRequest storageRequest) {
        Storage storage = Storage.builder()
                .type(storageRequest.getType())
                .address(storageRequest.getAddress())
                .build();
        Storage savedStorage = storageRepository.save(storage);
        return dtOservice.buildStorageDto(savedStorage,false);
    }


    public StorageDto getStorageById(Long storageId) {
        return dtOservice.buildStorageDto(findStorageById(storageId),false);
    }

    public StorageBriefDto getStorageBriefById(Long storageId) {
        return dtOservice.buildStorageBriefDto(findStorageById(storageId));
    }

    @Transactional
    public StorageDto moveStorage(StorageMovementRequest storageMovementRequest) {

        Storage currentStorage = findStorageById(storageMovementRequest.getCurrentStorageId());
        Storage newStorage = findStorageById(storageMovementRequest.getNewStorageId());

        StorageMovement storageMovement = storageMovementRepository.save(
                StorageMovement.builder()
                .currentStorage(currentStorage)
                .newStorage(newStorage)
                .status(StorageMovementStatus.PLACED)
                .shipmentStatus(ShipmentStatus.NOT_PLACED)
                .build());


        Set<StorageMovementItem> storageMovementItems = new HashSet<>();

        storageMovementRequest.getProductsQuantities().forEach( (key, value) -> {

            Product product = findProductById(key);

            StorageMovementItem storageMovementItem = StorageMovementItem.builder()
                    .product(product)
                    .quantity(value)
                    .accepted(false)
                    .storageMovement(storageMovement)
                    .build();

            storageMovementItems.add(storageMovementItemRepository.save(storageMovementItem));
        });

        storageMovement.setMovementItems(storageMovementItems);

        storageMovementRepository.save(storageMovement);

        /*
        Inventory inventoryLocation1 = inventoryLocationRepository.findById(storageMovementRequest.getCurrentStorageId()).orElseThrow();
        Inventory inventoryLocation2 = inventoryLocationRepository.findById(storageMovementRequest.getNewStorageId()).orElseThrow();

        if (inventoryLocation1.getQuantity() < storageMovementRequest.getQuantity()) {
            throw new RuntimeException("Quantity inside the inital storage is smaller than requested to move");
        } else {
            inventoryLocation1.removeQuantity(storageMovementRequest.getQuantity());
            inventoryLocation2.addQuantity(storageMovementRequest.getQuantity());
        }

        if (inventoryLocation1.getQuantity() == 0) {
            inventoryLocationRepository.delete(inventoryLocation1);
        }*/

        return dtOservice.buildStorageDto(currentStorage,false);
    }

    @Transactional
    public void deleteStorageById(Long storageId) {
        Storage storage = findStorageById(storageId);
        if (!storage.getInventories().isEmpty()) {
            throw new StorageNotEmptiedException("CLEAR INVENTORY FIRST");
        } else {
            storageRepository.delete(storage);
        }
    }

    @Transactional
    public Supplier updateSupplier(Long supplierId, SupplierRequest supplierRequest) {
            Supplier supplier = findSupplierById(supplierId);
            BeanUtils.copyProperties(supplierRequest,supplier);
            return supplierRepository.save(supplier);
    }

    public SupplierDtoWithShipments findSupplierWithShipmentsById(Long supplierId) {
        Supplier supplier =  findSupplierById(supplierId);

        return dtOservice.buildSupplierDtoWithShipments(supplier);
    }

    @Transactional
    public void deleteSupplierById(Long supplierId) {
        Supplier supplier = findSupplierById(supplierId);
        if (!supplier.getShipments().isEmpty()) {
            throw new RuntimeException("CLEAR SUPPLIER SHIPMENTS FIRST");
        } else {
            supplierRepository.delete(supplier);
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

        return dtOservice.buildShipmentWithListingsDto(supplierShipment);
    }

    public void deleteShipmentById(Long id) {
        shipmentRepository.deleteById(id);
    }

    public StorageDto getStorageWithProducts(Long id) {
        Storage storage = findStorageById(id);
        return dtOservice.buildStorageDto(storage, true);
    }

    @Transactional
    public InventoryLocationDto addWarehouseLocation(Long inventoryId, WarehouseLocationRequest warehouseLocationRequest) {

        Inventory inventory = findInventoryById(inventoryId);

        WarehouseLocation warehouseLocation = warehouseLocationRepository.save(
                WarehouseLocation.builder()
                        .section(warehouseLocationRequest.getSection())
                        .aisle(warehouseLocationRequest.getAisle())
                        .rack(warehouseLocationRequest.getRack())
                        .shelf(warehouseLocationRequest.getShelf())
                        .position(warehouseLocationRequest.getPosition())
                        .quantity(warehouseLocationRequest.getQuantity())
                        .inventory(inventory)
                        .build()
        );

        Set<WarehouseLocation> warehouseLocations = inventory.getWarehouseLocations();
        warehouseLocations.add(warehouseLocation);

        inventory.setWarehouseLocations(warehouseLocations);

        return dtOservice.buildInventoryLocationDto(inventoryRepository.save(inventory));
    }

    @Transactional
    public StorageDto finalizeMovement(Long movementId) {
        StorageMovement storageMovement = findStorageMovementById(movementId);
        storageMovement.setStatus(StorageMovementStatus.FINALIZED);

        Storage storage = storageMovement.getNewStorage();

        storageMovement.getMovementItems().forEach(item -> {
            Product product = item.getProduct();
            Inventory inventory = storage.getInventories().stream()
                    .filter(location -> location.getProduct().getId().equals(product.getId()))
                    .findFirst()
                    .orElseThrow();

            inventory.addQuantity(item.getQuantity());
            inventoryRepository.save(inventory);
        });

        storageMovement.setStatus(StorageMovementStatus.FINALIZED);
        return null;
    }

    @Transactional
    public StorageDto acceptMovement(Long movementId) {
        StorageMovement storageMovement = findStorageMovementById(movementId);
        storageMovement.setStatus(StorageMovementStatus.ACCEPTED);

        storageMovementRepository.save(storageMovement);

        return dtOservice.buildStorageDto(storageMovement.getCurrentStorage(), false);
    }

    @Transactional
    public void sendMovement(Long movementId) {
        StorageMovement storageMovement = findStorageMovementById(movementId);
        if (storageMovement.getStatus().equals(StorageMovementStatus.ACCEPTED)) {

            storageMovement.setStatus(StorageMovementStatus.SHIPPING);

            Storage storage = storageMovement.getCurrentStorage();

            storageMovement.getMovementItems().forEach(item -> {
                Product product = item.getProduct();
                Inventory inventory = storage.getInventories().stream()
                        .filter(location -> location.getProduct().getId().equals(product.getId()))
                        .findFirst()
                        .orElseThrow();

                inventory.removeQuantity(item.getQuantity());
                inventoryRepository.save(inventory);
            });

            storageMovementRepository.save(storageMovement);
        } else {
            throw new RuntimeException("CANNOT SEND MOVEMENT BEFORE IT IS ACCEPTED BY THE RECEIVING STORAGE");
        }
    }

    @Transactional
    public void changeMovementShipping(Long movementId, ShipmentStatus shipmentStatus) {
        StorageMovement storageMovement = findStorageMovementById(movementId);
        storageMovement.setShipmentStatus(shipmentStatus);

        storageMovementRepository.save(storageMovement);
    }

    @Transactional
    public void  editMovement(Long movementId, StorageMovementRequest storageMovementRequest) {
        StorageMovement storageMovement = findStorageMovementById(movementId);

        if (!storageMovement.getShipmentStatus().equals(ShipmentStatus.COMING)) {

            if (!storageMovement.getShipmentStatus().equals(ShipmentStatus.ISSUE)) {
                Storage newCurrentStorage = storageRepository.findById(storageMovementRequest.getCurrentStorageId()).orElseThrow();
                Storage newNewStorage = storageRepository.findById(storageMovementRequest.getNewStorageId()).orElseThrow();

                storageMovement.setCurrentStorage(newCurrentStorage);
                storageMovement.setNewStorage(newNewStorage);
            }

            Set<StorageMovementItem> storageMovementItems = new HashSet<>();

            storageMovementRequest.getProductsQuantities().forEach((key, value) -> {
                Product product = findProductById(key);

                storageMovementItems.add(
                        StorageMovementItem.builder()
                                .product(product)
                                .quantity(value)
                                .accepted(false)
                                .storageMovement(storageMovement)
                                .build()
                );
                storageMovement.setMovementItems(storageMovementItems);
            });

            storageMovementRepository.save(storageMovement);
        } else {
            throw new RuntimeException("CANNOT EDIT MOVEMENT IN SHIPMENT");
        }
    }

    public SupplierDto getSupplierById(Long supplierId) {
        Supplier supplier = findSupplierById(supplierId);
        return dtOservice.buildSupplierDto(supplier);
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

    private Supplier findSupplierById(Long id) {
        return supplierRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + id));
    }

    private SupplierShipment findSupplierShipmentById(Long id) {
        return shipmentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Supplier shipment not found with id: " + id));
    }

    private StorageMovement findStorageMovementById(Long id) {
        return storageMovementRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Storage movement not found with id: " + id));
    }

    public StorageMovementDto getMovementById(Long movementId) {
        return dtOservice.buildStorageMovementDto(findStorageMovementById(movementId));
    }


    public List<StorageMovementDto> getOutgoingMovementsBtnStorage(Long storageId) {
        Storage storage = findStorageById(storageId);
        return storageMovementRepository.findStorageMovementsByCurrentStorage(storage)
                .orElse(new ArrayList<>())
                    .stream()
                    .map(dtOservice::buildStorageMovementDto)
                    .collect(Collectors.toList());
    }

    public List<StorageMovementDto> getIncomingMovementsBtnStorage(Long storageId) {
        Storage storage = findStorageById(storageId);
        return storageMovementRepository.findStorageMovementsByNewStorage(storage)
                .orElse(new ArrayList<>())
                .stream()
                .map(dtOservice::buildStorageMovementDto)
                .collect(Collectors.toList());
    }
}