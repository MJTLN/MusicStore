package com.maciejjt.posinventory.model;

import com.maciejjt.posinventory.model.warehouse.Position;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"storage","product","positions","supplierShipments"})
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "STORAGE_ID", referencedColumnName = "ID")
    private Storage storage;
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
    private Product product;
    @ManyToMany
    @JoinTable(name = "INVENTORY_SHIPMENT",
            joinColumns = @JoinColumn(name = "INVENTORY_ID"),
            inverseJoinColumns = @JoinColumn(name = "SUPPLIER_SHIPMENT_ID")
    )
    private Set<SupplierShipment> supplierShipments;
    @OneToMany(mappedBy = "inventory")
    private Set<Position> positions;
    public void addQuantity(Integer amount) {
        this.quantity += amount;
    }

    public void removeQuantity(Integer amount) {
        this.quantity -= amount;
    }
}
