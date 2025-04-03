package com.maciejjt.posinventory.repository;

import com.maciejjt.posinventory.model.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    @Override
    @EntityGraph(attributePaths = {"products","purchaseIssue","products.label","payment"})
    Optional<Purchase> findById(Long id);

    @EntityGraph(attributePaths = {"products","purchaseIssue","products.label","payment"})
    Page<Purchase> findPurchasesByUser(User user, Pageable pageable);
}