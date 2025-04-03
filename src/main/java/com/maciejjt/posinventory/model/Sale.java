package com.maciejjt.posinventory.model;

import com.maciejjt.posinventory.model.enums.SaleStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @OneToMany(mappedBy = "sale")
    private Set<Discount> discounts;
    private Boolean isAggregating;
    @Enumerated(EnumType.STRING)
    private SaleStatus status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
