package com.maciejjt.posinventory.repository;

import com.maciejjt.posinventory.model.warehouse.Rack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RackReposiotry extends JpaRepository<Rack,Long> {
}
