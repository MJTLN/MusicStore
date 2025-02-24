package com.maciejjt.posinventory.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"product", "supplierShipment"})
public class SupplierShipmentItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "ID")
    private Product product;

    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "shipment_id", referencedColumnName = "ID")
    private SupplierShipment supplierShipment;

}