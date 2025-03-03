package com.maciejjt.posinventory.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    @ManyToOne
    @JoinColumn(name = "CART_ID", referencedColumnName = "ID")
    Cart cart;
    @OneToOne
    @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
    Product product;
    public int quantity;

    public void addQuantity(Integer amount) {
        this.quantity += amount;
    }

    public void removeQuantity(Integer amount) {
        this.quantity -= amount;
    }
}
