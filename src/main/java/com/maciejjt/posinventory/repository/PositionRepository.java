package com.maciejjt.posinventory.repository;

import com.maciejjt.posinventory.model.warehouse.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
}
