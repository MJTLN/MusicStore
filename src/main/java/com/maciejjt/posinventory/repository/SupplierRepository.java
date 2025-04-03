package com.maciejjt.posinventory.repository;

import com.maciejjt.posinventory.model.Supplier;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    @EntityGraph(attributePaths = {"shipments","shipments.supplierShipmentItems",
            "shipments.storage",
            "shipments.supplierShipmentItems.product",
            "shipments.supplierShipmentItems.product.label"})
    Optional<Supplier> findSupplierWShipmentsById(Long id);
}
