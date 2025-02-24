package com.maciejjt.posinventory.model.requests;

import com.maciejjt.posinventory.model.Discount;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class SaleRequest {
    private String name;
    private String description;
    private Set<Long> discountIds;
}
