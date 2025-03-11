package com.maciejjt.posinventory.model.warehouse;

import com.maciejjt.posinventory.model.Inventory;
import com.maciejjt.posinventory.model.Product;
import com.maciejjt.posinventory.model.Storage;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"shelf", "inventory"})
public class Position {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "SHELF_ID", referencedColumnName = "ID")
    private Shelf shelf;
    @ManyToOne
    @JoinColumn(name = "INVENTORY_ID")
    private Inventory inventory;
    private Integer quantity;
    private String number;

    public void addQuantity(Integer amount) {
        this.quantity += amount;
    }

    public void removeQuantity(Integer amount) {
        this.quantity -= amount;
    }
}
