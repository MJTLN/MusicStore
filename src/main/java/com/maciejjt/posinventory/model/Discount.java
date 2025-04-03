package com.maciejjt.posinventory.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
    private boolean isFixedValue;
    private boolean active;
    private Integer amount;
    @OneToOne(mappedBy = "discount")
    private Product product;
    private String name;
    @ManyToOne
    @JoinColumn(name = "SALE_ID", referencedColumnName = "ID")
    private Sale sale;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}