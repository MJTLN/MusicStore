package com.maciejjt.posinventory.admin;

import com.maciejjt.posinventory.model.dtos.CategoryDto;
import com.maciejjt.posinventory.model.requests.CategoryRequest;
import com.maciejjt.posinventory.service.CategoryService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@Data
@RequestMapping("/admin/category")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Void> createCategory(@RequestBody CategoryRequest categoryRequest) {
         Long id = categoryService.createCategory(categoryRequest).getId();
         URI location = URI.create("/admin/category/" + id);
         return ResponseEntity.created(location).build();
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> addProductsToCategory(@RequestBody List<Long> productIds, @PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.addProductsToCategory(productIds,categoryId));
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}