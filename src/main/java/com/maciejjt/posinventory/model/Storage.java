package com.maciejjt.posinventory.model;

import com.maciejjt.posinventory.model.enums.InventoryLocationType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"inventories","warehouseLayout","supplierShipments","incomingMovements","outMovements"})
public class Storage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private InventoryLocationType type;
    private String address;
    @OneToOne
    @JoinColumn(name = "WAREHOUSE_LAYOUT_ID", referencedColumnName = "ID")
    private WarehouseLayout warehouseLayout;
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
