package com.maciejjt.posinventory.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "inventory")
public class WarehouseLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne()
    @JoinColumn(name = "INVENTORY_ID", referencedColumnName = "ID")
    private Inventory inventory;
    @ManyToOne()
    @JoinColumn(name = "STORAGE_ID", referencedColumnName = "ID")
    private Storage storage;
    private Integer section;
    private Integer aisle;
    // 0 OZNACZA CAŁĄ WYPEŁNIONĄ CZESC
    private Integer rack;
    private Integer shelf;
    private Integer position;
    private Integer quantity;

    public void addQuantity(Integer amount) {
        this.quantity += amount;
    }

    public void removeQuantity(Integer amount) {
        this.quantity -= amount;
    }
}
