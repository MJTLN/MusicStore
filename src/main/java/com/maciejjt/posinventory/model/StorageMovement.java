package com.maciejjt.posinventory.model;

import com.maciejjt.posinventory.model.enums.ShipmentStatus;
import com.maciejjt.posinventory.model.enums.StorageMovementStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"movementItems","currentStorage","newStorage"})
public class StorageMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @OneToMany(mappedBy = "storageMovement")
    Set<StorageMovementItem> movementItems;
    @ManyToOne()
    @JoinColumn(name = "CURRENT_STORAGE_ID", referencedColumnName = "ID")
    Storage currentStorage;
    @ManyToOne()
    @JoinColumn(name = "NEW_STORAGE_ID", referencedColumnName = "ID")
    Storage newStorage;
    String note;
    StorageMovementStatus status;
    ShipmentStatus shipmentStatus;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
