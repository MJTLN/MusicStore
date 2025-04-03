package com.maciejjt.posinventory.repository;

import com.maciejjt.posinventory.model.SupplierShipment;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShipmentRepository extends JpaRepository<SupplierShipment, Long>, JpaSpecificationExecutor<SupplierShipment> {
    @EntityGraph(attributePaths = {"supplierShipmentItems","supplier","supplierShipmentItems.product","supplierShipmentItems.product.label","storage"})
    Optional<SupplierShipment> findSupplierShipmentWListingsById(Long id);
}
