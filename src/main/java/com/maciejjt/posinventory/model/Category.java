package com.maciejjt.posinventory.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"parentCategory","childCategories","products"})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID")
    private Category parentCategory;
    @OneToMany(mappedBy = "parentCategory")
    @BatchSize(size = 20)
    private Set<Category> childCategories;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "CATEGORY_PRODUCT",
            joinColumns = @JoinColumn(name = "CATEGORY_ID"),
            inverseJoinColumns = @JoinColumn(name = "PRODUCT_ID")
    )
    private Set<Product> products;

}