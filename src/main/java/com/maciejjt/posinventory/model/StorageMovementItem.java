package com.maciejjt.posinventory.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"storageMovement","product"})
public class StorageMovementItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne()
    @JoinColumn(name = "STORAGE_MOVEMENT_ACCEPTATION_ID", referencedColumnName = "ID")
    StorageMovement storageMovement;
    @OneToOne()
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
    Product product;
    Integer quantity;
    boolean accepted;
}