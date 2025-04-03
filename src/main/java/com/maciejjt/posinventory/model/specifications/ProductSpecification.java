package com.maciejjt.posinventory.model.specifications;

import com.maciejjt.posinventory.model.*;
import com.maciejjt.posinventory.model.requests.ProductSearchRequest;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecification {
    public static Specification<Product> filterByDetails(ProductSearchRequest request) {
        return (root, query, criteriaBuilder) -> {
            Join<Product, ProductDetail> productDetailJoin = root.join("productDetails", JoinType.INNER);
            List<Predicate> predicates = new ArrayList<>();

            if (request.getCategoryId() != null) {
                Join<Product, Category> join = root.join("categories", JoinType.INNER);
                predicates.add(criteriaBuilder.equal(join.get("id"), request.getCategoryId()));
            }

            if (request.getLabels() != null) {
                request.getLabels().forEach(label ->
                        predicates.add(criteriaBuilder.isMember(label, root.get("label"))));
            }

            if (request.getMinPrice() != null && request.getMaxPrice() != null) {
                predicates.add(criteriaBuilder.between(root.get("price"), request.getMinPrice(), request.getMaxPrice()));
            }

            if (request.getExactValue() != null) {
                List<Predicate> tempPredicates = new ArrayList<>();
                request.getExactValue().forEach((key, value) -> tempPredicates.add(criteriaBuilder.and(
                        criteriaBuilder.equal(productDetailJoin.get("name"), key),
                        criteriaBuilder.equal(productDetailJoin.get("value"), value)
                )));
                predicates.add(criteriaBuilder.or(tempPredicates.toArray(new Predicate[0])));
            }

            if (request.getMultipleValues() != null) {
                List<Predicate> tempPredicates = new ArrayList<>();
                request.getMultipleValues().forEach((key, value) ->
                        tempPredicates.add(criteriaBuilder.and(
                                criteriaBuilder.equal(productDetailJoin.get("name"), key),
                                productDetailJoin.get("value").in(value)
                        ))
                );
                predicates.add(criteriaBuilder.or(tempPredicates.toArray(new Predicate[0])));
            }

            query.groupBy(root.get("id"));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}













