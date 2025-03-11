package com.maciejjt.posinventory.model;

import com.maciejjt.posinventory.model.warehouse.*;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Data
public class WarehouseLayout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "STORAGE_ID", referencedColumnName = "ID")
    private Storage storage;
    @OneToMany(mappedBy = "warehouseLayout", fetch = FetchType.LAZY)
    private Set<Section> sections;
}
