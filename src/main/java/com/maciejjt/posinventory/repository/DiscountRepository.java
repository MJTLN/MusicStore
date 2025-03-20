package com.maciejjt.posinventory.repository;

import com.maciejjt.posinventory.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    List<Discount> findDiscountByActiveAndStartDateIsAfter(Boolean isActive, LocalDateTime startDate);
    List<Discount> findDiscountByActiveAndEndDateIsAfter(Boolean isActive, LocalDateTime endDate);
}
