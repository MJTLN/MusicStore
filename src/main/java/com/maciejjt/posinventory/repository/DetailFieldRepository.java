package com.maciejjt.posinventory.repository;

import com.maciejjt.posinventory.model.DetailField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailFieldRepository extends JpaRepository<DetailField, Long>, JpaSpecificationExecutor<DetailField> {
}
