package com.maciejjt.posinventory.service;

import com.maciejjt.posinventory.exceptions.EntityNotFoundException;
import com.maciejjt.posinventory.model.Cart;
import com.maciejjt.posinventory.model.CartProduct;
import com.maciejjt.posinventory.model.Product;
import com.maciejjt.posinventory.model.User;
import com.maciejjt.posinventory.model.api.dtos.CartDto;
import com.maciejjt.posinventory.repository.CartProductRepository;
import com.maciejjt.posinventory.repository.CartRepository;
import com.maciejjt.posinventory.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Data
public class CartService {

    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final ProductRepository productRepository;
    private final DTOservice dtoService;

    @Transactional
    public void addToCart(Long productId, Integer quantity, User user) {

        Cart cart = findCartByUser(user);

        Optional<CartProduct> existingCartProduct = cart.getProducts().stream()
                .filter(cartProduct -> cartProduct.getProduct().getId().equals(productId))
                .findFirst();

        if (existingCartProduct.isPresent()) {

            CartProduct cartProduct = existingCartProduct.get();
            cartProduct.addQuantity(quantity);
            cartProductRepository.save(cartProduct);

        } else {
            Product product = findProductById(productId);

            CartProduct newCartProduct = CartProduct.builder()
                    .product(product)
                    .quantity(quantity)
                    .cart(cart)
                    .build();

            CartProduct cartProduct = cartProductRepository.save(newCartProduct);

            cart.getProducts().add(cartProduct);
            cartRepository.save(cart);
        }
    }

    public CartDto getCartByUser(User user) {
        Cart cart = findCartByUser(user);
        return dtoService.buildCartDto(cart);
    }

    @Transactional
    public void updateCartProductQuantity(Long productId, boolean addOrRemove, Integer quantity, User user) {

        Cart cart = findCartByUser(user);
        Optional<CartProduct> cartProduct = cart.getProducts().stream()
                .filter(entry -> entry.getProduct().getId().equals(productId))
                .findFirst();

        if (cartProduct.isEmpty()) {
            throw new EntityNotFoundException("No product in cart with id " + productId);
        }

        if (addOrRemove) {
            cartProduct.get().addQuantity(quantity);
        } else {
            cartProduct.get().removeQuantity(quantity);
        }

        cartProductRepository.save(cartProduct.get());
    }

    @Transactional
    public void deleteProductFromCart(Long productId, User user) {
        Cart cart = findCartByUser(user);
        Optional<CartProduct> cartProduct = cart.getProducts().stream()
                .filter(entry -> entry.getProduct().getId().equals(productId))
                .findFirst();

        cartProduct.ifPresent(product -> {
                    cart.getProducts().remove(product);
                    cartProductRepository.delete(product);
                }
        );
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found with id " + id));
    }

    public Cart findCartByUser(User user) {
        return cartRepository.findCartByUser(user).orElseThrow(() -> new EntityNotFoundException("No cart found for user with id " + user.getId()));
    }
}
