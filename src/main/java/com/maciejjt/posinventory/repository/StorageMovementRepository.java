package com.maciejjt.posinventory.repository;

import com.maciejjt.posinventory.model.Storage;
import com.maciejjt.posinventory.model.StorageMovement;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StorageMovementRepository extends JpaRepository<StorageMovement, Long> {
    @EntityGraph(attributePaths = {"currentStorage","newStorage","movementItems","movementItems.product"})
    Optional<List<StorageMovement>> findStorageMovementsByCurrentStorage(Storage storage);

    @EntityGraph(attributePaths = {"currentStorage","newStorage","movementItems","movementItems.product"})
    Optional<List<StorageMovement>> findStorageMovementsByNewStorage(Storage storage);

    @EntityGraph(attributePaths = {"currentStorage","newStorage","movementItems","movementItems.product"})
    Optional<StorageMovement> findStorageMovementWithProductsById(Long id);
}
