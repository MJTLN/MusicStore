package com.maciejjt.posinventory.api;

import com.maciejjt.posinventory.exceptions.AuthenticationException;
import com.maciejjt.posinventory.model.DetailField;
import com.maciejjt.posinventory.model.User;
import com.maciejjt.posinventory.model.api.dtos.ApiProductDto;
import com.maciejjt.posinventory.model.api.dtos.CartDto;
import com.maciejjt.posinventory.model.dtos.DetailFieldDto;
import com.maciejjt.posinventory.model.dtos.ProductListingDto;
import com.maciejjt.posinventory.model.dtos.SaleDtoWithProducts;
import com.maciejjt.posinventory.model.requests.ProductSearchRequest;
import com.maciejjt.posinventory.repository.UserRepository;
import com.maciejjt.posinventory.service.CustomUserDetails;
import com.maciejjt.posinventory.service.DetailFieldService;
import com.maciejjt.posinventory.service.DiscountService;
import com.maciejjt.posinventory.service.ProductService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@Data
@RequestMapping("/product")
public class ApiProductController {

    private final ProductService productService;
    private final UserRepository userRepository;
    private final DiscountService discountService;
    private final DetailFieldService detailFieldService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiProductDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.findApiProductById(id));
    }

    //API PRODUCT
    @PostMapping("/search")
    public ResponseEntity<List<ProductListingDto>> searchProducts(@RequestBody ProductSearchRequest productSearchRequest) {
        return ResponseEntity.ok(productService.findProductListingsByDetails(productSearchRequest));
    }

    @GetMapping("/{categoryId}")
    private ResponseEntity<List<DetailFieldDto>> getDetailsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(detailFieldService.findDetailFieldByCategory(categoryId));
    }

    @PostMapping("{productId}/cart")
    public ResponseEntity<Void> addProductToCart(@PathVariable Long productId, @RequestParam Integer quantity, Authentication authentication) {
        User user = findUser(authentication);
        productService.addToCart(productId, quantity,user);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("{productId}/cart")
    public ResponseEntity<Void> updateCartProductQuantity(@PathVariable Long productId, @RequestParam Boolean addOrRemove, @RequestParam Integer quantity, Authentication authentication) {
        User user = findUser(authentication);
        productService.updateCartProductQuantity(productId, addOrRemove,quantity,user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productId}/cart")
    public ResponseEntity<Void> deleteProductFromCart(@PathVariable Long productId, Authentication authentication) {
        User user = findUser(authentication);
        productService.deleteProductFromCart(productId, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cart")
    public ResponseEntity<CartDto> getUserCart(Authentication authentication) {
        User user = findUser(authentication);
        return ResponseEntity.ok(productService.getCartByUser(user));
    }

    @GetMapping("/sales")
    public ResponseEntity<Set<SaleDtoWithProducts>> getCurrentSales() {
        return ResponseEntity.ok(discountService.getCurrentSales());
    }

    @GetMapping
    private User findUser(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userRepository.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new AuthenticationException("There was a error authenticating the user"));
    }
}