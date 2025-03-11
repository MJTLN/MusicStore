package com.maciejjt.posinventory.model.warehouse;

import com.maciejjt.posinventory.model.Storage;
import com.maciejjt.posinventory.model.WarehouseLayout;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"warehouseLayout", "aisles"})
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "WAREHOUSE_LAYOUT_ID", referencedColumnName = "ID")
    private WarehouseLayout warehouseLayout;
    private String number;
    @OneToMany(mappedBy = "section")
    Set<Aisle> aisles;
}
