package com.maciejjt.posinventory.api;

import com.maciejjt.posinventory.model.dtos.SaleDtoWithProducts;
import com.maciejjt.posinventory.service.DiscountService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@Data
@RequestMapping("/sale")
public class ApiSaleController {

    private final DiscountService discountService;

    @GetMapping()
    public ResponseEntity<Set<SaleDtoWithProducts>> getCurrentSales() {
        return ResponseEntity.ok(discountService.getCurrentSales());
    }
}
