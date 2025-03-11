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
@EqualsAndHashCode(exclude = {"section", "racks"})
public class Aisle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String number;
    @ManyToOne
    @JoinColumn(name = "SECTION_ID", referencedColumnName = "ID")
    private Section section;
    @OneToMany(mappedBy = "aisle")
    Set<Rack> racks;
}
