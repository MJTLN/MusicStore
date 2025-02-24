package com.maciejjt.posinventory.repository;

import com.maciejjt.posinventory.model.DetailField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailFieldRepository extends JpaRepository<DetailField, Long> {
}
