package com.maciejjt.posinventory.repository;

import com.maciejjt.posinventory.model.Inventory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    @EntityGraph(attributePaths = {"supplierShipments","supplierShipments.supplierShipmentItems"})
    Optional<Inventory> findInventoryWithShipmentsById(Long id);
}
