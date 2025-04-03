package com.maciejjt.posinventory.model;

import com.maciejjt.posinventory.model.enums.ProductLabel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

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
    private Integer UPC;
    @ElementCollection()
    @CollectionTable(name = "product_label", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "label")
    @Enumerated(EnumType.STRING)
    @BatchSize(size = 10)
    private Set<ProductLabel> label;
    private String description;
    private String descriptionShort;
    private BigDecimal price;
    @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "DISCOUNT_ID", referencedColumnName = "ID")
    private Discount discount;
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<Inventory> inventories;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<ProductDetail> productDetails;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "CATEGORY_PRODUCT",
            joinColumns = @JoinColumn(name = "PRODUCT_ID"),
            inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID")
    )
    private Set<Category> categories;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}