package com.maciejjt.posinventory.admin;

import com.maciejjt.posinventory.model.dtos.*;
import com.maciejjt.posinventory.model.requests.ProductRequest;
import com.maciejjt.posinventory.model.requests.ProductSearchRequest;
import com.maciejjt.posinventory.model.enums.ProductLabel;
import com.maciejjt.posinventory.service.DiscountService;
import com.maciejjt.posinventory.service.InventoryService;
import com.maciejjt.posinventory.service.ProductService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


@RestController
@Data
@RequestMapping("/admin/product")
public class AdminProductController {

    private final ProductService productService;

    @PostMapping()
    public ResponseEntity<Long> createProduct(@RequestBody ProductRequest productRequest) {
        Long id = productService.createProduct(productRequest).getId();
        URI location = URI.create("/admin/product/" + id);
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProductById(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(productService.updateProductById(id, productRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/by-upc")
    public ResponseEntity<ProductDto> findProductByUPC(@RequestParam Integer UPC) {
        return ResponseEntity.ok(productService.findProductByUPC(UPC));
    }

    @PostMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProducts(@RequestBody ProductSearchRequest productSearchRequest) {
        return ResponseEntity.ok(productService.findProductsByDetails(productSearchRequest));
    }

    @PostMapping("/search/listings")
    public ResponseEntity<List<ProductListingDto>> searchProductsListings(@RequestBody ProductSearchRequest productSearchRequest) {
        return ResponseEntity.ok(productService.findProductListingsByDetails(productSearchRequest));
    }

    @PatchMapping("/{productId}/labels/{label}")
    public ResponseEntity<ProductDto> addLabelToProduct(@PathVariable Long productId, @PathVariable ProductLabel label) {
        return ResponseEntity.ok(productService.addLabelToProduct(label, productId));
    }

    @DeleteMapping("/{productId}/labels/{label}")
    public ResponseEntity<ProductDto> removeLabelFromProduct(@PathVariable Long productId, @PathVariable ProductLabel label) {
        return ResponseEntity.ok(productService.removeLabelFromProduct(label, productId));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}