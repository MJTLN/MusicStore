package com.maciejjt.posinventory.api;

import com.maciejjt.posinventory.exceptions.AuthenticationException;
import com.maciejjt.posinventory.model.User;
import com.maciejjt.posinventory.model.api.dtos.CartDto;
import com.maciejjt.posinventory.repository.UserRepository;
import com.maciejjt.posinventory.service.CustomUserDetails;
import com.maciejjt.posinventory.service.ProductService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@Data
@RequestMapping("/cart")
public class ApiCartController {

    private final UserRepository userRepository;
    private final ProductService productService;
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

    private User findUser(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userRepository.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new AuthenticationException("There was a error authenticating the user"));
    }
}
