package com.maciejjt.posinventory.repository;

import com.maciejjt.posinventory.model.WarehouseLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseLocationRepository extends JpaRepository<WarehouseLocation, Long> {
}
