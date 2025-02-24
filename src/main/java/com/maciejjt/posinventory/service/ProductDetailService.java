package com.maciejjt.posinventory.service;

import com.maciejjt.posinventory.model.DetailField;
import com.maciejjt.posinventory.model.Product;
import com.maciejjt.posinventory.model.ProductDetail;
import com.maciejjt.posinventory.repository.DetailFieldRepository;
import com.maciejjt.posinventory.repository.ProductDetailRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Data
public class ProductDetailService {

    private final ProductDetailRepository productDetailRepository;
    private final DetailFieldService detailFieldService;

    @Transactional
    public Set<ProductDetail> addDetails(Map<Long,String> details, Product product) {

        Set<ProductDetail> productDetails = new HashSet<>();

        details.forEach((id, value) -> {
            DetailField detailField = detailFieldService.findById(id);
            ProductDetail productDetail = ProductDetail.builder()
                    .detailField(detailField)
                    .product(product)
                    .name(detailField.getName())
                    .value(value)
                    .build();
            productDetails.add(productDetail);
        });

        productDetailRepository.saveAll(productDetails);

        return productDetails;
    }
}
