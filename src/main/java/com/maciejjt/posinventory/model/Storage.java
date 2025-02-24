package com.maciejjt.posinventory.model;

import com.maciejjt.posinventory.model.enums.InventoryLocationType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"inventories"})
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private InventoryLocationType type;
    private String address;
    @OneToMany(mappedBy = "storage")
    Set<Inventory> inventories;
    @OneToMany(mappedBy = "storage")
    Set<SupplierShipment> supplierShipments;
    @OneToMany(mappedBy = "newStorage")
    Set<StorageMovement> incomingMovements;
    @OneToMany(mappedBy = "currentStorage")
    Set<StorageMovement> outMovements;

    public void addIncomingMovement(StorageMovement storageMovement) {
        this.incomingMovements.add(storageMovement);
    }

    public void addOutMovement(StorageMovement storageMovement) {
        this.outMovements.add(storageMovement);
    }
}
