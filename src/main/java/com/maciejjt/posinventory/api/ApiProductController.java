package com.maciejjt.posinventory.api;

import com.maciejjt.posinventory.model.api.dtos.ApiProductDto;
import com.maciejjt.posinventory.model.dtos.ProductListingDto;
import com.maciejjt.posinventory.model.requests.ProductSearchRequest;
import com.maciejjt.posinventory.service.ProductService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Data
@RequestMapping("/product")
public class ApiProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiProductDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findApiProductById(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductListingDto>> getProductListingsByCategory(
            @PathVariable Long categoryId, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productService.findProductListingsByCategory(categoryId, page, size));
    }

    @PostMapping("/search")
    public ResponseEntity<Page<ProductListingDto>> searchProducts(@RequestBody ProductSearchRequest productSearchRequest,
                                                                  @RequestParam int page,
                                                                  @RequestParam int size) {
        return ResponseEntity.ok(productService.findProductListingsByDetails(productSearchRequest, page, size));
    }

    @PostMapping("/search/listings")
    public ResponseEntity<Page<ProductListingDto>> searchProductsListings(@org.springframework.web.bind.annotation.RequestBody ProductSearchRequest productSearchRequest,
                                                                          @RequestParam int page,
                                                                          @RequestParam int size) {
        return ResponseEntity.ok(productService.findProductListingsByDetails(productSearchRequest, page, size));
    }
}