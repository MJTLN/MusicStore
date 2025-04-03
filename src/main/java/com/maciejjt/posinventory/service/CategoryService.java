package com.maciejjt.posinventory.service;

import com.maciejjt.posinventory.exceptions.EntityNotFoundException;
import com.maciejjt.posinventory.model.Category;
import com.maciejjt.posinventory.model.Product;
import com.maciejjt.posinventory.model.dtos.CategoryDto;
import com.maciejjt.posinventory.model.requests.CategoryRequest;
import com.maciejjt.posinventory.repository.CategoryRepository;
import com.maciejjt.posinventory.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Data
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final DTOservice dtOservice;

    public List<Category> findAllById(Set<Long> ids) {
        return categoryRepository.findAllById(ids);
    }

    @Transactional
    public Category createCategory(CategoryRequest categoryRequest) {

        Category.CategoryBuilder builder = Category.builder();

        builder.name(categoryRequest.getName());

        if(categoryRequest.getParentId() != null ) {
            Category parentCategory = findCategoryById(categoryRequest.getParentId());
            builder.parentCategory(parentCategory);
        }

        if(categoryRequest.getChildrenId() != null && !categoryRequest.getChildrenId().isEmpty()) {
            Set<Category> childCategories = new HashSet<>();
            categoryRequest.getChildrenId().forEach( id -> {
                Category childCategory = findCategoryById(id);
                childCategories.add(childCategory);
            });
            builder.childCategories(childCategories);
        }

        Category category = builder.build();

        Category savedCategory = categoryRepository.save(category);

        if (savedCategory.getChildCategories() != null) {
            savedCategory.getChildCategories().forEach(item ->{
                item.setParentCategory(savedCategory);
                categoryRepository.save(item);
            });
        }

        return savedCategory;
     }


     @Transactional
    public CategoryDto addProductsToCategory(List<Long> productIds, Long categoryId) {
        Set<Product> products = new HashSet<>();

        productIds.forEach(id -> {
            Product product = findProductById(id);
            products.add(product);
        });

        Category category = findCategoryById(categoryId);

        addProductsToCategory(products, category);

        return dtOservice.buildCategoryDto(category);
    }

    @Transactional
    public void addProductsToCategory(Set<Product> products, Category category) {
        category.getProducts().addAll(products);
        categoryRepository.save(category);

        if (category.getParentCategory() != null) {
            addProductsToCategory(products, category.getParentCategory());
        }
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found with id " + id));
    }

    public List<CategoryDto> getPrimaryCategories() {
       List<Category> categories = categoryRepository.getCategoriesByParentCategoryIsNull();
       return categories.stream()
               .map(dtOservice::buildCategoryDto)
               .toList();
    }

    public List<CategoryDto> getChildCategoriesById(Long categoryId) {
        Category category = categoryRepository.findCategoryWChildrenById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id " + categoryId));

        return category.getChildCategories().stream()
                .map(dtOservice::buildCategoryDto)
                .toList();
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found with id " + id));
    }

    public void deleteCategory(Long categoryId) {
        Category category = findCategoryById(categoryId);
        categoryRepository.delete(category);
    }
}
