package com.maciejjt.posinventory.model.warehouse;

import com.maciejjt.posinventory.model.Storage;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"aisle", "shelves"})
public class Rack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;
    @ManyToOne
    @JoinColumn(name = "AISLE_ID", referencedColumnName = "ID")
    private Aisle aisle;
    @OneToMany(mappedBy = "rack")
    Set<Shelf> shelves;
}
