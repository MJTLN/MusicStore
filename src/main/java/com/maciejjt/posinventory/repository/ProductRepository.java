package com.maciejjt.posinventory.repository;

import com.maciejjt.posinventory.model.Category;
import com.maciejjt.posinventory.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>, JpaSpecificationExecutor<Product> {

    @EntityGraph(attributePaths = {"productDetails.detailField", "label", "discount","discount.sale"})
    Optional<Product> findProductForApiById(Long id);


    @EntityGraph(attributePaths = {"productDetails.detailField", "label", "inventories", "discount"})
    Optional<Product> findProductForAdminById(Long id);

    @EntityGraph(attributePaths = {"productDetails.detailField", "label", "inventories"})
    Optional<Product> findProductByUPC(Integer UPC);

    @EntityGraph(attributePaths = {"productDetails.detailField", "label", "discount","discount.sale"})
    Page<Product> findProductByCategoriesContaining(Category category, Pageable pageable);
}