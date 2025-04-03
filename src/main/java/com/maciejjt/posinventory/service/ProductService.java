package com.maciejjt.posinventory.service;

import com.maciejjt.posinventory.exceptions.DeletionException;
import com.maciejjt.posinventory.exceptions.EntityNotFoundException;
import com.maciejjt.posinventory.model.*;
import com.maciejjt.posinventory.model.api.dtos.ApiProductDto;
import com.maciejjt.posinventory.model.dtos.*;
import com.maciejjt.posinventory.model.requests.ProductRequest;
import com.maciejjt.posinventory.model.requests.ProductSearchRequest;
import com.maciejjt.posinventory.model.enums.ProductLabel;
import com.maciejjt.posinventory.model.specifications.ProductSpecification;
import com.maciejjt.posinventory.model.specifications.ShipmentSpecification;
import com.maciejjt.posinventory.repository.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final ShipmentRepository shipmentRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Product createProduct(ProductRequest productRequest) {

        Set<Category> categories = new HashSet<>(categoryService.findAllById(productRequest.getCategoryIds()));

        Product product = Product.builder()
                .name(productRequest.getName())
                .UPC(productRequest.getUPC())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
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
        addedProduct.setCategories(categories);

        return productRepository.save(addedProduct);

    }

    public ProductDto getProductById(Long id) {
        Product product = productRepository.findProductForAdminById(id).orElseThrow(() -> new EntityNotFoundException("Product not found with id " + id));
        return dtOservice.buildProductDto(product);
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found with id " + id));
    }

    public Page<ProductDto> findProductsByDetails(ProductSearchRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Specification<Product> specification = ProductSpecification.filterByDetails(request);
        Page<Product> products = productRepository.findAll(specification, pageable);
        return products.map(dtOservice::buildProductDto);
    }

    public Page<ProductListingDto> findProductListingsByDetails(ProductSearchRequest request, int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Specification<Product> specification = ProductSpecification.filterByDetails(request);
        Page<Product> products = productRepository.findAll(specification, pageable);
        return products.map(dtOservice::buildProductListingDto);
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

       throw new DeletionException("The specified product does not have such label at the moment");
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
        Specification<SupplierShipment> specification = ShipmentSpecification.findShipmentWithProduct(productId);
        List<SupplierShipment> supplierShipments = shipmentRepository.findAll(specification);
        if (!supplierShipments.isEmpty()) {
            throw new DeletionException("There are still shipments for this product that are not marked as finished");
        }
        productRepository.deleteById(productId);
    }

    public ProductDto findProductByUPC(Integer upc) {
            Product product = productRepository.findProductByUPC(upc).orElseThrow(() -> new EntityNotFoundException("No product has been found with UPC " + upc));
            return dtOservice.buildProductDto(product);
    }

    public ApiProductDto findApiProductById(Long id) {
        Product product = findProductForApi(id);
        return dtOservice.buildApiProductDto(product);
    }

    public Page<ProductListingDto> findProductListingsByCategory(Long id, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Category category = findCategoryById(id);
        return productRepository.findProductByCategoriesContaining(category,pageable)
                .map(dtOservice::buildProductListingDto);
    }

    private Product findProductForApi(Long id) {
        return productRepository.findProductForApiById(id).orElseThrow(() -> new EntityNotFoundException("Product not found with id " + id));
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Category not found with id " + id));
    }

}