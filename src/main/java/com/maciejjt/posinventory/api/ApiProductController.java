package com.maciejjt.posinventory.api;

import com.maciejjt.posinventory.model.api.dtos.ApiProductDto;
import com.maciejjt.posinventory.model.dtos.ProductListingDto;
import com.maciejjt.posinventory.model.requests.ProductSearchRequest;
import com.maciejjt.posinventory.service.ProductService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Data
@RequestMapping("/product")
public class ApiProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiProductDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findApiProductById(id));
    }

    //API PRODUCT
    @PostMapping("/search")
    public ResponseEntity<List<ProductListingDto>> searchProducts(@RequestBody ProductSearchRequest productSearchRequest) {
        return ResponseEntity.ok(productService.findProductListingsByDetails(productSearchRequest));
    }


}