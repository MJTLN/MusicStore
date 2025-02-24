package com.maciejjt.posinventory.admin;

import com.maciejjt.posinventory.model.dtos.DiscountDto;
import com.maciejjt.posinventory.model.dtos.SaleDtoWithProducts;
import com.maciejjt.posinventory.model.requests.DiscountRequest;
import com.maciejjt.posinventory.model.dtos.SaleDto;
import com.maciejjt.posinventory.model.requests.SaleRequest;
import com.maciejjt.posinventory.service.DiscountService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@Data
@RequestMapping("/admin/discount")
public class AdminDiscountController {

    private final DiscountService discountService;

    //DISCOUNTS

    @PostMapping("/{productId}")
    public ResponseEntity<Void> createDiscountForProduct(@PathVariable Long productId, @RequestBody DiscountRequest discountRequest) {
        discountService.addDiscountToProduct(productId, discountRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{discountId}")
    public ResponseEntity<DiscountDto> updateDiscount(@PathVariable Long discountId, @RequestBody DiscountRequest discountRequest) {
        return ResponseEntity.ok(discountService.updateDiscount(discountId, discountRequest));
    }

    @GetMapping("/{discountId}")
    public ResponseEntity<DiscountDto> getDiscountById(@PathVariable Long discountId) {
        return ResponseEntity.ok(discountService.getDiscountById(discountId));
    }

    @DeleteMapping("/{discountId}")
    public ResponseEntity<Void> deleteDiscountById(@PathVariable Long discountId) {
        discountService.deleteDiscount(discountId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/discounts")
    public ResponseEntity<Void> deleteDiscountsById(@RequestBody Set<Long> discountIds) {
        discountService.deleteDiscounts(discountIds);
        return ResponseEntity.ok().build();
    }

    //SALES

    @PostMapping("/sale")
    public ResponseEntity<Long> createSale(@RequestBody SaleRequest saleRequest) {
        return ResponseEntity.ok(discountService.createSale(saleRequest).getId());
    }

    @PostMapping("/sale/{saleId}")
    public ResponseEntity<Void> addDiscountsToSale(@PathVariable Long saleId, @RequestBody List<Long> discountIds) {
        discountService.addDiscountsToSale(saleId, discountIds);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/sale/{saleId}")
    public ResponseEntity<SaleDto> findSaleById(@PathVariable Long saleId) {
        return ResponseEntity.ok(discountService.getSaleById(saleId));
    }

    @GetMapping("/sale/{saleId}/withProducts")
    public ResponseEntity<SaleDtoWithProducts> getSaleWithProducts(@PathVariable Long saleId) {
        return ResponseEntity.ok(discountService.getSaleDtoWithProducts(saleId));
    }

    @PutMapping("/sale/{saleId}")
    public ResponseEntity<SaleDto> updateSale(@PathVariable Long saleId, @RequestBody SaleRequest saleRequest) {
        return ResponseEntity.ok(discountService.updateSale(saleId, saleRequest));
    }

    @DeleteMapping("/sale/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id, boolean withDiscounts) {
        discountService.deleteSale(id,true);
        return ResponseEntity.ok().build();
    }

}