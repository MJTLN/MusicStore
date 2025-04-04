package com.maciejjt.posinventory.model;

import com.maciejjt.posinventory.model.enums.InventoryType;
import jakarta.persistence.*;
import lombok.*;

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
    private InventoryType type;
    private String address;
    @OneToOne(fetch = FetchType.LAZY)
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

}