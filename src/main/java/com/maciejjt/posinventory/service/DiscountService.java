package com.maciejjt.posinventory.service;

import com.maciejjt.posinventory.exceptions.EntityNotFoundException;
import com.maciejjt.posinventory.model.*;
import com.maciejjt.posinventory.model.dtos.DiscountDto;
import com.maciejjt.posinventory.model.dtos.SaleDtoWithProducts;
import com.maciejjt.posinventory.model.requests.DiscountRequest;
import com.maciejjt.posinventory.model.dtos.SaleDto;
import com.maciejjt.posinventory.model.requests.SaleRequest;
import com.maciejjt.posinventory.repository.DiscountRepository;
import com.maciejjt.posinventory.repository.ProductRepository;
import com.maciejjt.posinventory.repository.SaleRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Data
public class DiscountService {

    private final ProductRepository productRepository;
    private final DiscountRepository discountRepository;
    private final SaleRepository saleRepository;
    private final DTOservice dtOservice;

    @Transactional
    public void addDiscountToProduct(Long productId, DiscountRequest discountRequest) {

        Product product = productRepository.findById(productId)
                .orElseThrow();

        Discount discount = Discount.builder()
                .name(discountRequest.getName())
                .isFixedAmount(discountRequest.isFixedAmount())
                .amount(discountRequest.getAmount())
                .startDate(discountRequest.getStartDate())
                .endDate(discountRequest.getEndDate())
                .product(product)
                .build();

        Discount createdDiscount = discountRepository.save(discount);

        product.setDiscount(createdDiscount);

        productRepository.save(product);
    }

    @Transactional
    public Sale createSale(SaleRequest saleRequest) {

        Sale sale = Sale.builder()
                .name(saleRequest.getName())
                .description(saleRequest.getDescription())
                .build();

        Sale savedSale = saleRepository.save(sale);

        return addDiscountsToSale(savedSale.getId(), saleRequest.getDiscountIds());
    }

    @Transactional
    public Sale addDiscountsToSale(Long saleId, Iterable<Long> discountIds) {

        Sale sale = saleRepository.findById(saleId).orElseThrow();

        Set<Discount> discounts = new HashSet<>();

        discountIds.forEach(id -> {
            Discount discount = discountRepository.findById(id).orElseThrow();
            discount.setSale(sale);
            discounts.add(discount);
        });

        sale.setDiscounts(discounts);

        return saleRepository.save(sale);
    }

    public SaleDto getSaleById(Long saleId) {
        Sale sale = findSaleById(saleId);
        return dtOservice.buildSaleDto(sale);
    }


    @Transactional
    public void deleteDiscount(Long id) {
        Discount discount = findDiscountById(id);
        Product product = findProductById(discount.getProduct().getId());
        product.setDiscount(null);
        productRepository.save(product);

        discountRepository.deleteById(id);
    }

    @Transactional
    public DiscountDto updateDiscount(Long id, DiscountRequest discountRequest) {
        Discount discount = findDiscountById(id);
        BeanUtils.copyProperties(discountRequest,discount);
        if(discountRequest.getSaleId() != null) {
            Sale sale = findSaleById(discountRequest.getSaleId());
            discount.setSale(sale);
        }
        return dtOservice.buildDiscountDto(discountRepository.save(discount));
    }

    public DiscountDto getDiscountById(Long id) {
        Discount discount = findDiscountById(id);
        return dtOservice.buildDiscountDto(discount);
    }

    @Transactional
    public void deleteDiscounts(Set<Long> discountIds) {
        List<Discount> discounts = discountRepository.findAllById(discountIds);
        discountRepository.deleteAll(discounts);
    }

    @Transactional
    public void deleteSale(Long id, boolean withDiscounts) {

        Sale sale = findSaleById(id);

        if (withDiscounts) {
            sale.getDiscounts().forEach(discount -> {
                Product product = discount.getProduct();
                product.setDiscount(null);
                productRepository.save(product);
                discountRepository.delete(discount);
            });
        }

        saleRepository.delete(sale);
    }

    @Transactional
    public SaleDto updateSale(Long id, SaleRequest saleRequest) {
        Sale sale = findSaleById(id);
        BeanUtils.copyProperties(saleRequest, sale);
        Set<Discount> discounts = new HashSet<>();

        saleRequest.getDiscountIds().forEach(discountId -> {
                Discount discount = findDiscountById(discountId);
                discount.setSale(sale);
                discountRepository.save(discount);
                discounts.add(discount);

        });

        sale.setDiscounts(discounts);

        return dtOservice.buildSaleDto(saleRepository.save(sale));
    }

    public SaleDtoWithProducts getSaleDtoWithProducts(Long id) {
        Sale sale = findSaleById(id);
        return dtOservice.buildSaleDtoWithProducts(sale);
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    private Discount findDiscountById(Long id) {
        return discountRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Discount not found with id: " + id));
    }

    private Sale findSaleById(Long id) {
        return saleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Sale not found with id: " + id));
    }
}