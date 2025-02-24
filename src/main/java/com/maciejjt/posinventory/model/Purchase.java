package com.maciejjt.posinventory.model;


import com.maciejjt.posinventory.model.enums.PurchaseStatus;
import com.maciejjt.posinventory.model.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"user","payment","products","purchaseIssue"})
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //bidirectional
    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    private User user;
    private BigDecimal amount;
    @OneToOne
    @JoinColumn(name = "PAYMENT_ID", referencedColumnName = "ID")
    private Payment payment;
    private ShipmentStatus shipmentStatus;
    private PurchaseStatus purchaseStatus;
    //onedirectional
    @ManyToMany
    @JoinTable(
            name = "PURCHASE_PRODUCTS",
            joinColumns = @JoinColumn(name = "PURCHASE_ID"),
            inverseJoinColumns = @JoinColumn(name = "PRODUCT_ID")
    )
    private Set<Product> products;
    @OneToOne(mappedBy = "purchase")
    private PurchaseIssue purchaseIssue;
    private String shipper;
    private String country;
    private String city;
    private String street;
    private String house;
    private String apartment;
    private String phone;
    private String mail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}


