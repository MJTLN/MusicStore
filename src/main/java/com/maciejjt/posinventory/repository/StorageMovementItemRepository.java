package com.maciejjt.posinventory.repository;

import com.maciejjt.posinventory.model.StorageMovementItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageMovementItemRepository extends JpaRepository<StorageMovementItem, Long> {
}
