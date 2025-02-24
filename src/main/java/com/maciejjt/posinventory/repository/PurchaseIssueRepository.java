package com.maciejjt.posinventory.repository;

import com.maciejjt.posinventory.model.PurchaseIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseIssueRepository extends JpaRepository<PurchaseIssue, Long> {
}
