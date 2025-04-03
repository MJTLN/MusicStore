package com.maciejjt.posinventory.repository;

import com.maciejjt.posinventory.model.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    @EntityGraph(attributePaths = "childCategories")
    List<Category> getCategoriesByParentCategoryIsNull();

    @EntityGraph(attributePaths = {"parentCategory","childCategories"})
    Optional<Category> findCategoryWChildrenById(Long id);
}
