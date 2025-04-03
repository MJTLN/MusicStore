package com.maciejjt.posinventory.admin;

import com.maciejjt.posinventory.model.dtos.SaleDto;
import com.maciejjt.posinventory.model.dtos.SaleDtoWithProducts;
import com.maciejjt.posinventory.model.requests.SaleRequest;
import com.maciejjt.posinventory.service.DiscountService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@Data
@RequestMapping("/admin/sale")
public class AdminSaleController {

    private final DiscountService discountService;

    @PostMapping()
    public ResponseEntity<Long> createSale(@RequestBody SaleRequest saleRequest) {
        Long id = discountService.createSale(saleRequest).getId();
        URI location = URI.create("/admin/discount/sale" + id);
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/{saleId}")
    public ResponseEntity<Void> addDiscountsToSale(@PathVariable Long saleId, @RequestBody List<Long> discountIds) {
        discountService.addDiscountsToSale(saleId, discountIds);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{saleId}")
    public ResponseEntity<Void> endSale(@PathVariable Long saleId) {
        discountService.endSale(saleId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{saleId}")
    public ResponseEntity<SaleDto> findSaleById(@PathVariable Long saleId) {
        return ResponseEntity.ok(discountService.getSaleById(saleId));
    }

    @GetMapping("/{saleId}/withProducts")
    public ResponseEntity<SaleDtoWithProducts> getSaleWithProducts(@PathVariable Long saleId) {
        return ResponseEntity.ok(discountService.getSaleDtoWithProducts(saleId));
    }

    @PutMapping("/{saleId}")
    public ResponseEntity<SaleDto> updateSale(@PathVariable Long saleId, @RequestBody SaleRequest saleRequest) {
        return ResponseEntity.ok(discountService.updateSale(saleId, saleRequest));
    }

    @DeleteMapping("/{saleId}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long saleId, @RequestParam boolean deleteDiscounts, @RequestBody List<Long> discountsExcludedFromDeletion) {
        discountService.deleteSale(saleId, deleteDiscounts, discountsExcludedFromDeletion);
        return ResponseEntity.noContent().build();
    }
}
