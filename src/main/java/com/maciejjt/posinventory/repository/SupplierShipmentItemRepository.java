package com.maciejjt.posinventory.repository;

import com.maciejjt.posinventory.model.SupplierShipmentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierShipmentItemRepository extends JpaRepository<SupplierShipmentItem, Long> {
}
