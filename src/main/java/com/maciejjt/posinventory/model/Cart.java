package com.maciejjt.posinventory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @OneToOne()
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    User user;
    @OneToMany(mappedBy = "cart")
    Set<CartProduct> products;
}
