package com.maciejjt.posinventory.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "shipments")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;
    private String officialAddress;
    private String contactInfo;
    @OneToMany(mappedBy = "supplier")
    private Set<SupplierShipment> shipments;
}