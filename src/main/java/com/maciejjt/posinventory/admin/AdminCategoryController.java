package com.maciejjt.posinventory.admin;

import com.maciejjt.posinventory.model.dtos.CategoryDto;
import com.maciejjt.posinventory.model.requests.CategoryRequest;
import com.maciejjt.posinventory.service.CategoryService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Data
@RequestMapping("/admin/category")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Void> createCategory(@RequestBody CategoryRequest categoryRequest) {
         categoryService.createCategory(categoryRequest);
         return ResponseEntity.ok().build();
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> addProductsToCategory(@RequestBody List<Long> productIds, @PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.addProductsToCategory(productIds,categoryId));
    }


}