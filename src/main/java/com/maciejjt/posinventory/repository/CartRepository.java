package com.maciejjt.posinventory.repository;

import com.maciejjt.posinventory.model.Cart;
import com.maciejjt.posinventory.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @EntityGraph(attributePaths = {"products","products.product"})
    Optional<Cart> findCartByUser(User user);
}
