package com.maciejjt.posinventory.repository;

import com.maciejjt.posinventory.model.Sale;
import com.maciejjt.posinventory.model.enums.SaleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    List<Sale> findSalesByStatus(SaleStatus status);
    List<Sale> findSaleByStatusAndStartDateIsAfter(SaleStatus status, LocalDateTime startDate);
    List<Sale> findSaleByStatusAndEndDateIsAfter(SaleStatus status, LocalDateTime endDate);
}
