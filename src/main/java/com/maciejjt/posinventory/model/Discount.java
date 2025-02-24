package com.maciejjt.posinventory.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"sale","product"})
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean isFixedAmount;
    private Long amount;
    @OneToOne(mappedBy = "discount")
    private Product product;
    private String name;
    @ManyToOne
    @JoinColumn(name = "SALE_ID", referencedColumnName = "ID")
    private Sale sale;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}