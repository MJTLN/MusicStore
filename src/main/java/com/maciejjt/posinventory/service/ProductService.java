package com.maciejjt.posinventory.service;

import com.maciejjt.posinventory.exceptions.EntityNotFoundException;
import com.maciejjt.posinventory.model.*;
import com.maciejjt.posinventory.model.api.dtos.ApiProductDto;
import com.maciejjt.posinventory.model.api.dtos.CartDto;
import com.maciejjt.posinventory.model.dtos.*;
import com.maciejjt.posinventory.model.requests.ProductRequest;
import com.maciejjt.posinventory.model.requests.ProductSearchRequest;
import com.maciejjt.posinventory.model.enums.ProductLabel;
import com.maciejjt.posinventory.model.specifications.ProductSpecification;
import com.maciejjt.posinventory.repository.CartProductRepository;
import com.maciejjt.posinventory.repository.CartRepository;
import com.maciejjt.posinventory.repository.ProductRepository;
import com.maciejjt.posinventory.repository.StorageRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Data
public class ProductService {

    private final ProductRepository productRepository;
    private final StorageRepository storageRepository;
    private final CategoryService categoryService ;
    private final InventoryService inventoryService;
    private final ProductDetailService productDetailService;
    private final DTOservice dtOservice;
    private final CartProductRepository cartProductRepository;
    private final CartRepository cartRepository;

    @Transactional
    public Product createProduct(ProductRequest productRequest) {

        Set<Category> categories = new HashSet<>(categoryService.findAllById(productRequest.getCategoryIds()));

        Product product = Product.builder()
                .name(productRequest.getName())
                .UPC(productRequest.getUPC())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .categories(categories)
                .description(productRequest.getDescription())
                .descriptionShort(productRequest.getDescriptionShort())
                .label(productRequest.getLabels())
                .price(new BigDecimal(productRequest.getPrice()))
                .build();

        Product addedProduct = productRepository.save(product);

        Set<ProductDetail> productDetails = new HashSet<>(
                productDetailService.addDetails(productRequest.getProductDetails(), addedProduct)
        );

        addedProduct.setProductDetails(productDetails);

        return productRepository.save(addedProduct);

    }

    public ProductDto getProductById(Long id) {
        return dtOservice.buildProductDto(findProductById(id));
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found with id " + id));
    }

    public List<ProductDto> findProductsByDetails(ProductSearchRequest request) {
        Specification<Product> specification = ProductSpecification.filterByDetails(request);
        List<Product> products = productRepository.findAll(specification);
        List<ProductDto> productDtos = new ArrayList<>();
        products.forEach(product -> productDtos.add(dtOservice.buildProductDto(product)));
        return productDtos;
    }

    public List<ProductListingDto> findProductListingsByDetails(ProductSearchRequest request) {
        Specification<Product> specification = ProductSpecification.filterByDetails(request);
        List<Product> products = productRepository.findAll(specification);
        List<ProductListingDto> productListingDtos = new ArrayList<>();
        products.forEach(product -> productListingDtos.add(dtOservice.buildProductListingDto(product)));
        return productListingDtos;
    }

    @Transactional
    public ProductDto addLabelToProduct(ProductLabel label, Long productId) {
        Product product = findProductById(productId);
        product.getLabel().add(label);

        return dtOservice.buildProductDto(productRepository.save(product));
    }

    @Transactional
    public ProductDto removeLabelFromProduct(ProductLabel label, Long productId) {
        Product product = findProductById(productId);

        if (product.getLabel().contains(label)) {
            product.getLabel().remove(label);
            return dtOservice.buildProductDto(productRepository.save(product));
        }

        //ROZBUDOWAC
       throw new RuntimeException("Selected product has no label like this");
    }

    @Transactional
    public ProductDto updateProductById(Long id, ProductRequest productRequest) {
        Product product = findProductById(id);
        BeanUtils.copyProperties(productRequest, product, "id","productDetails","categoryIds");
        product.setUpdatedAt(LocalDateTime.now());

        Set<Category> foundCategories = new HashSet<>(categoryService.findAllById(productRequest.getCategoryIds()));
        Set<Category> categories = product.getCategories();
        categories.addAll(foundCategories);
        product.setCategories(categories);

        Set<ProductDetail> newProductDetails = new HashSet<>(
                productDetailService.addDetails(productRequest.getProductDetails(), product)
        );

        Set<ProductDetail> productDetails = product.getProductDetails();
        productDetails.addAll(newProductDetails);
        product.setProductDetails(productDetails);

        BeanUtils.copyProperties(productRequest, product, "id","productDetails","categoryIds");

        return dtOservice.buildProductDto(productRepository.save(product));
    }

    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    public ProductDto findProductByUPC(Long upc) {
            Product product = productRepository.findProductByUPC(upc).orElseThrow(() -> new EntityNotFoundException("No product has been found with UPC " + upc));
            return dtOservice.buildProductDto(product);
    }

    public ApiProductDto findApiProductById(Long id) {
        Product product = findProductById(id);
        return dtOservice.buildApiProductDto(product);
    }

    @Transactional
    public void addToCart(Long productId, Integer quantity, User user) {

        Cart cart = user.getCart();

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
        Cart cart = user.getCart();
        return dtOservice.buildCartDto(cart);
    }

    @Transactional
    public void updateCartProductQuantity(Long productId, boolean addOrRemove, Integer quantity, User user) {

        Cart cart = user.getCart();
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
        Cart cart = user.getCart();
        Optional<CartProduct> cartProduct = cart.getProducts().stream()
                .filter(entry -> entry.getProduct().getId().equals(productId))
                .findFirst();

        cartProduct.ifPresent(product -> {
                    cart.getProducts().remove(product);
                    cartProductRepository.delete(product);
                }
        );
    }
}