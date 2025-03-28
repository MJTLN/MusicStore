package com.maciejjt.posinventory.api;

import com.maciejjt.posinventory.model.dtos.CategoryDto;
import com.maciejjt.posinventory.model.dtos.DetailFieldDto;
import com.maciejjt.posinventory.service.CategoryService;
import com.maciejjt.posinventory.service.DetailFieldService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Data
@RequestMapping("/category")
public class ApiCategoryController {

    private final CategoryService categoryService;
    private final DetailFieldService detailFieldService;

    @GetMapping()
    public ResponseEntity<List<CategoryDto>> getPrimaryCategories() {
        return ResponseEntity.ok(categoryService.getPrimaryCategories());
    }

    @GetMapping("/{categoryId}/children")
    public ResponseEntity<List<CategoryDto>> getChildCategoriesById(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getChildCategoriesById(categoryId));
    }

    @GetMapping("/{categoryId}/details")
    private ResponseEntity<List<DetailFieldDto>> getDetailsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(detailFieldService.findDetailFieldByCategory(categoryId));
    }
}