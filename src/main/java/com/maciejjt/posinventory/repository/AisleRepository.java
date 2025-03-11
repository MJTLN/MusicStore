package com.maciejjt.posinventory.repository;

import com.maciejjt.posinventory.model.warehouse.Aisle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AisleRepository extends JpaRepository<Aisle, Long> {
}
