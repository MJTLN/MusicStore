package com.maciejjt.posinventory.model;

import com.maciejjt.posinventory.model.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"supplier","supplierShipmentItems","storage"})
public class SupplierShipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "SUPPLIER_ID", referencedColumnName = "ID")
    private Supplier supplier;
    private LocalDateTime createdAt;
    private LocalDateTime arrivalTime;
    @OneToMany(mappedBy = "supplierShipment", cascade = CascadeType.REMOVE)
    private Set<SupplierShipmentItem> supplierShipmentItems;
    private BigDecimal amount;
    private ShipmentStatus status;
    private String note;
    @ManyToOne
    @JoinColumn(name = "STORAGE_ID", referencedColumnName = "ID")
    Storage storage;
}
