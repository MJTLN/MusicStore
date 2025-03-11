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
@EqualsAndHashCode(exclude = {"rack", "positions"})
public class Shelf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;
    @ManyToOne
    @JoinColumn(name = "RACK_ID", referencedColumnName = "ID")
    private Rack rack;
    @OneToMany(mappedBy = "shelf")
    private Set<Position> positions;

}
