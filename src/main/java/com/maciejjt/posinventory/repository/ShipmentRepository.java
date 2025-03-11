package com.maciejjt.posinventory.repository;

import com.maciejjt.posinventory.model.SupplierShipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<SupplierShipment, Long>, JpaSpecificationExecutor<SupplierShipment> {
}
