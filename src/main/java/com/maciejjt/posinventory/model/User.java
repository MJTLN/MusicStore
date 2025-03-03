package com.maciejjt.posinventory.model;

import com.maciejjt.posinventory.model.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
@EqualsAndHashCode(exclude = {"addresses","purchases"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String phone;
    private String email;
    private Role role;
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Address> addresses;
    @JsonIgnore
    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Set<Purchase> purchases;
    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Cart cart;
}