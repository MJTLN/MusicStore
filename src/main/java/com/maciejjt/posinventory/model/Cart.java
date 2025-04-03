package com.maciejjt.posinventory.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"user","products"})
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
