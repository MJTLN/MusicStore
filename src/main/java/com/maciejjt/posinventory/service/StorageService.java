package com.maciejjt.posinventory.service;

import com.maciejjt.posinventory.exceptions.DeletionException;
import com.maciejjt.posinventory.exceptions.EntityNotFoundException;
import com.maciejjt.posinventory.exceptions.WarehouseConflictException;
import com.maciejjt.posinventory.model.Inventory;
import com.maciejjt.posinventory.model.Storage;
import com.maciejjt.posinventory.model.WarehouseLayout;
import com.maciejjt.posinventory.model.dtos.*;
import com.maciejjt.posinventory.model.requests.StorageRequest;
import com.maciejjt.posinventory.model.requests.WarehouseLayoutRequest;
import com.maciejjt.posinventory.model.warehouse.*;
import com.maciejjt.posinventory.repository.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Data
public class StorageService {

    private final StorageRepository storageRepository;
    private final WarehouseLayoutRepository warehouseLayoutRepository;
    private final SectionRepository sectionRepository;
    private final AisleRepository aisleRepository;
    private final RackReposiotry rackReposiotry;
    private final ShelfRepository shelfRepository;
    private final PositionRepository positionRepository;
    private final InventoryRepository inventoryRepository;
    private final DTOservice dtoService;

    public WarehouseLayoutDtoDetailed getWarehouseLayoutDetailedForStorage(Long storageId) {
        Storage storage = findStorageById(storageId);
        return dtoService.buildWarehouseLayoutDetailedDto(storage.getWarehouseLayout());
    }

    public WarehouseLayoutDto getWarehouseLayoutForStorage(Long storageId) {
        Storage storage = findStorageById(storageId);
        return dtoService.buildWarehouseLayoutDto(storage.getWarehouseLayout());
    }

    public Set<PositionDto> getPositionsForInventory(Long inventoryId) {
        Inventory inventory = findInventoryById(inventoryId);
        return inventory.getPositions().stream()
                .map(dtoService::buildPositionDto)
                .collect(Collectors.toSet());
    }

    @Transactional
    public void movePosition(Long currentPositionId, Long newPositionId) {
        Position currentPosition = findPositionById(currentPositionId);
        Position newPosition = findPositionById(newPositionId);
        if (!currentPosition.getStorage().equals(newPosition.getStorage())) {
            throw new WarehouseConflictException("Supplied positions with ids: " + currentPosition.getId() + ", " + newPosition.getId() + " are in different storages");
        }
        if (newPosition.getInventory() != null || newPosition.getQuantity() != null) {
            throw new WarehouseConflictException("Cannot move stock to a position that's not emptied");
        }
        if (currentPosition.getInventory() == null || currentPosition.getQuantity() == 0) {
            throw new WarehouseConflictException("Cannot move stock from a empty position");
        }
        newPosition.setInventory(currentPosition.getInventory());
        newPosition.setQuantity(currentPosition.getQuantity());
        currentPosition.setQuantity(null);
        currentPosition.setInventory(null);

        positionRepository.save(currentPosition);
        positionRepository.save(newPosition);
    }

    @Transactional
    public void updatePositionProductQuantity(Long positionId, Integer quantity, boolean add) {
        Position position = findPositionById(positionId);
        if (position.getInventory() == null) {
            throw new WarehouseConflictException("Cannot update quantity on a empty position. Place a product there first");
        }
        if (add) {
            position.addQuantity(quantity);
        } else {
            if (quantity > position.getQuantity()) {
                throw new WarehouseConflictException("The specified quantity is larger than current quantity at this position - " + position.getQuantity());
            }
            position.removeQuantity(quantity);
            if (position.getQuantity() == 0) {
                position.setQuantity(null);
                position.setInventory(null);
            }
        }
        positionRepository.save(position);
    }

    public Position findPositionById(Long positionId) {
        return positionRepository.findById(positionId).orElseThrow(() -> new EntityNotFoundException("position not found with id " + positionId));
    }

    public Shelf findShelfById(Long id) {
        return shelfRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Shelf not found with id " + id));
    }

    public Rack findRackById(Long id) {
        return rackReposiotry.findById(id).orElseThrow(() -> new EntityNotFoundException("Rack not found with id " + id));
    }

    @Transactional
    public void putProductsOnRack(Long rackId, Long inventoryId, Integer quantity) {
        Rack rack = findRackById(rackId);
        Integer dividedQuantity = quantity/rack.getShelves().size();
        rack.getShelves().forEach( shelf -> {
            putProductOnShelf(shelf, inventoryId, dividedQuantity);
        });
    }

    @Transactional
    public void putProductOnShelf(Shelf shelf, Long inventoryId, Integer quantity) {
        Integer dividedQuantity = quantity/shelf.getPositions().size();
        shelf.getPositions().forEach( position -> {
            putProductOnPosition(position, inventoryId, dividedQuantity);
        });
    }

    @Transactional
    public void putProductsOnShelf(Long shelfId, Long inventoryId, Integer quantity) {
        Shelf shelf = findShelfById(shelfId);
        Integer dividedQuantity = quantity/shelf.getPositions().size();
        shelf.getPositions().forEach( position -> {
            putProductOnPosition(position, inventoryId, dividedQuantity);
        });
    }

    @Transactional
    public void putProductOnPosition(Position position, Long inventoryId, Integer quantity) {
        Inventory inventory = findInventoryById(inventoryId);
        position.setInventory(inventory);
        position.setQuantity(quantity);
        positionRepository.save(position);
    }

    @Transactional
    public void putProductOnPosition(Long positionId, Long inventoryId, Integer quantity) {
        Position position = positionRepository.findById(positionId).orElseThrow(() -> new EntityNotFoundException("Position not found with id " + positionId));
        Inventory inventory = findInventoryById(inventoryId);

        if (position.getInventory() != null) {
            if (position.getInventory().equals(inventory))   {
                updatePositionProductQuantity(positionId,quantity,true);
            }
        }

        if (position.getQuantity() == null || position.getQuantity().equals(0)) {
            position.setInventory(inventory);
            position.setQuantity(quantity);
            positionRepository.save(position);
        } else {
            throw new WarehouseConflictException("Position with id " + position + " is already occupied");
        }

    }

    @Transactional
    public WarehouseLayout createWarehouseLayout(WarehouseLayoutRequest request) {
        Storage storage = findStorageById(request.getStorageId());
        WarehouseLayout warehouseLayout =  warehouseLayoutRepository.save(WarehouseLayout.builder()
                .storage(storage)
                .build()
        );
        storage.setWarehouseLayout(warehouseLayout);
        storageRepository.save(storage);

        Set<Section> sections = new HashSet<>();

        request.getLayout().forEach((sectionNumber, aisles) -> {
            Section section = sectionRepository.save(Section.builder()
                    .number(sectionNumber)
                    .warehouseLayout(storage.getWarehouseLayout())
                    .build()
            );

            sections.add(section);

            aisles.forEach(((aisleNumber, racks) -> {
                Aisle aisle = aisleRepository.save(Aisle.builder()
                        .section(section)
                        .number(sectionNumber + "-" + aisleNumber)
                        .build()
                );

                racks.forEach((rackNumber, shelves) -> {
                    Rack rack = rackReposiotry.save(Rack.builder()
                            .number(sectionNumber + "-" + aisleNumber + "_" + rackNumber)
                            .aisle(aisle)
                            .build()
                    );

                    shelves.forEach((shelfNumber) -> {
                        Shelf shelf = shelfRepository.save(Shelf.builder()
                                .rack(rack)
                                .number(sectionNumber + "-" + aisleNumber + "_" + rackNumber + "-" + shelfNumber)
                                .build()
                        );
                        for (int i = 1; i < 4 ; i++) {
                            positionRepository.save(Position.builder()
                                    .shelf(shelf)
                                    .storage(storage    )
                                    .number(sectionNumber + "-" + aisleNumber + "_" + rackNumber + "-" + shelfNumber + "-" + i)
                                    .build());
                        }
                    });

                });
            }));
        });

        warehouseLayout.setSections(sections);
        return warehouseLayoutRepository.save(warehouseLayout);
    }

    @Transactional
    public void deleteStorageById(Long storageId ) {
        Storage storage = findStorageById(storageId);
        if (!storage.getInventories().isEmpty()) {
            throw new DeletionException("Inventories must be cleared before deleting storage");
        } else {
            storageRepository.delete(storage);
        }
    }

    public StorageBriefDto getStorageBriefById(Long storageId) {
        return dtoService.buildStorageBriefDto(findStorageById(storageId));
    }

    public StorageDto getStorageWithProducts(Long id) {
        Storage storage = findStorageById(id);
        return dtoService.buildStorageDto(storage, true);
    }

    public StorageDto getStorageById(Long storageId) {
        return dtoService.buildStorageDto(findStorageById(storageId),false);
    }

    public Storage createStorage(StorageRequest storageRequest) {
        Storage storage = Storage.builder()
                .type(storageRequest.getType())
                .address(storageRequest.getAddress())
                .build();
        return storageRepository.save(storage);
    }

    private Storage findStorageById(Long id) {
        return storageRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Storage not found with id: " + id));
    }

    private Inventory findInventoryById(Long id) {
        return inventoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Inventory not found with id: " + id));
    }
}