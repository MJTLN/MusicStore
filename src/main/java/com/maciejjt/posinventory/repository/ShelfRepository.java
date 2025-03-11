package com.maciejjt.posinventory.repository;

import com.maciejjt.posinventory.model.warehouse.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShelfRepository extends JpaRepository<Shelf,Long> {
}
