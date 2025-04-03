package com.maciejjt.posinventory.service;

import com.maciejjt.posinventory.exceptions.BadStatusException;
import com.maciejjt.posinventory.exceptions.EntityNotFoundException;
import com.maciejjt.posinventory.model.*;
import com.maciejjt.posinventory.model.dtos.StorageDto;
import com.maciejjt.posinventory.model.dtos.StorageMovementDto;
import com.maciejjt.posinventory.model.enums.ShipmentStatus;
import com.maciejjt.posinventory.model.enums.StorageMovementStatus;
import com.maciejjt.posinventory.model.requests.PositionRequest;
import com.maciejjt.posinventory.model.requests.StorageMovementRequest;
import com.maciejjt.posinventory.model.warehouse.Position;
import com.maciejjt.posinventory.repository.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Data
public class StorageMovementService {

    private final InventoryRepository inventoryRepository;
    private final PositionRepository positionRepository;
    private final StorageMovementRepository storageMovementRepository;
    private final StorageMovementItemRepository storageMovementItemRepository;
    private final StorageRepository storageRepository;
    private final ProductRepository productRepository;
    private final DTOservice dtoService;

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
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
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

        return dtoService.buildStorageDto(currentStorage,false);
    }

    public List<StorageMovementDto> getOutgoingMovementsBtnStorage(Long storageId) {
        Storage storage = findStorageById(storageId);
        return storageMovementRepository.findStorageMovementsByCurrentStorage(storage)
                .orElse(new ArrayList<>())
                    .stream()
                    .sorted(Comparator.comparing(StorageMovement::getUpdatedAt))
                    .map(dtoService::buildStorageMovementDto)
                    .collect(Collectors.toList());
    }

    public List<StorageMovementDto> getIncomingMovementsBtnStorage(Long storageId) {
        Storage storage = findStorageById(storageId);
        return storageMovementRepository.findStorageMovementsByNewStorage(storage)
                .orElse(new ArrayList<>())
                .stream()
                .sorted(Comparator.comparing(StorageMovement::getUpdatedAt))
                .map(dtoService::buildStorageMovementDto)
                .collect(Collectors.toList());
    }

    public StorageMovementDto getMovementById(Long movementId) {
        return dtoService.buildStorageMovementDto(storageMovementRepository.findStorageMovementWithProductsById(movementId)
                .orElseThrow(() -> new EntityNotFoundException("Storage movement not found with id " + movementId)));
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

            storageMovement.setUpdatedAt(LocalDateTime.now());
            storageMovementRepository.save(storageMovement);
        } else {
            throw new BadStatusException("Cannot edit movement in shipment");
        }
    }

    @Transactional
    public void changeMovementShipping(Long movementId, ShipmentStatus shipmentStatus) {
        StorageMovement storageMovement = findStorageMovementById(movementId);
        storageMovement.setShipmentStatus(shipmentStatus);

        storageMovementRepository.save(storageMovement);
    }

    @Transactional
    public void sendMovement(Long movementId, Set<PositionRequest> positions) {

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

            positions.forEach(item -> {
                Position position = findPositionById(item.getPositionId());
                position.removeQuantity(item.getAmount());
                positionRepository.save(position);
            });

            storageMovement.setUpdatedAt(LocalDateTime.now());
            storageMovementRepository.save(storageMovement);
        } else {
            throw new BadStatusException("Cannot send a movement before it is accepted by the receiving storage");
        }
    }

    @Transactional
    public StorageDto acceptMovement(Long movementId) {
        StorageMovement storageMovement = findStorageMovementById(movementId);
        storageMovement.setStatus(StorageMovementStatus.ACCEPTED);

        storageMovementRepository.save(storageMovement);
        storageMovement.setUpdatedAt(LocalDateTime.now());

        return dtoService.buildStorageDto(storageMovement.getCurrentStorage(), false);
    }

    @Transactional
    public StorageDto finalizeMovement(Long movementId, Set<PositionRequest> positions) {
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

        positions.forEach(item -> {
            Position position = positionRepository.findById(item.getPositionId()).orElseThrow(() -> new EntityNotFoundException("No position found with id " + item.getPositionId()));
            position.addQuantity(item.getAmount());
        });

        storageMovement.setUpdatedAt(LocalDateTime.now());
        storageMovement.setStatus(StorageMovementStatus.FINALIZED);

        return dtoService.buildStorageDto(storageMovementRepository.save(storageMovement).getNewStorage(),false);
    }

    private StorageMovement findStorageMovementById(Long id) {
        return storageMovementRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Storage movement not found with id: " + id));
    }
    private Storage findStorageById(Long id) {
        return storageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Storage not found with id: " + id));
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    public Position findPositionById(Long positionId) {
        return positionRepository.findById(positionId).orElseThrow(() -> new EntityNotFoundException("position not found with id " + positionId));
    }
}