package com.maciejjt.posinventory.model.dtos;

import com.maciejjt.posinventory.model.Category;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class DetailFieldDto {
    private Long id;
    private String name;
    private String values;
}
