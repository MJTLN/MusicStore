package com.maciejjt.posinventory.model;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "categories")
public class DetailField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @JdbcTypeCode(SqlTypes.JSON)
    private String values;
    @ManyToMany
    @JoinTable(
            name = "DETAIL_FIELD_CATEGORY",
            joinColumns = @JoinColumn(name = "DETAIL_FIELD_ID"),
            inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID")
    )
    private List<Category> categories;

    public void setValuesList(List<String> values) throws JsonProcessingException {
        this.values = new ObjectMapper().writeValueAsString(values);
    }
}