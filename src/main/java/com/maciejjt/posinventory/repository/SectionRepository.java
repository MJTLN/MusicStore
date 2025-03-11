package com.maciejjt.posinventory.repository;

import com.maciejjt.posinventory.model.warehouse.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
}
