package com.maciejjt.posinventory.repository;

import com.maciejjt.posinventory.model.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
}
