package com.maciejjt.posinventory.model;

import com.maciejjt.posinventory.model.enums.ProductLabel;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(exclude = {"inventories", "productDetails", "categories", "discount"})
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long UPC;
    @ElementCollection()
    @CollectionTable(name = "product_label", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "label")
    @Enumerated(EnumType.STRING)
    private Set<ProductLabel> label;
    private String description;
    private String descriptionShort;
    private BigDecimal price;
    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "DISCOUNT_ID", referencedColumnName = "ID")
    private Discount discount;
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private Set<Inventory> inventories;
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Set<ProductDetail> productDetails;
    @ManyToMany(mappedBy = "products")
    private Set<Category> categories;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}