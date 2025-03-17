package com.maciejjt.posinventory.model.specifications;

import com.maciejjt.posinventory.model.Category;
import com.maciejjt.posinventory.model.DetailField;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class DetailFieldSpecification {

    public static Specification<DetailField> filterByCategory(Long categoryId) {
        return ((root, query, criteriaBuilder) -> {
            Join<DetailField, Category> join = root.join("categories");
            return criteriaBuilder.equal(join.get("id"), categoryId);
        });
    }
}
