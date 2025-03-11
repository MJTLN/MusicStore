package com.maciejjt.posinventory.model.specifications;

import com.maciejjt.posinventory.model.Product;
import com.maciejjt.posinventory.model.SupplierShipment;
import com.maciejjt.posinventory.model.SupplierShipmentItem;
import com.maciejjt.posinventory.model.enums.ShipmentStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ShipmentSpecification {
    public static Specification<SupplierShipment> findShipmentWithProduct(Long productId) {
        return ((root, query, criteriaBuilder) -> {
            Join<SupplierShipment, SupplierShipmentItem> initialJoin = root.join("supplierShipmentItems", JoinType.INNER);
            Join<SupplierShipmentItem, Product> productJoin = initialJoin.join("product", JoinType.INNER);
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.notEqual(root.get("status"), ShipmentStatus.COMPLETED));
            predicates.add(criteriaBuilder.equal(productJoin.get("id"),productId));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
    }
}
