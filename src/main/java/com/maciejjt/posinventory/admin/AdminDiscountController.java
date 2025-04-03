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

import java.net.URI;
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
        Long id = discountService.addDiscountToProduct(productId, discountRequest).getId();
        URI location = URI.create("/admin/discount/" + id);
        return ResponseEntity.created(location).build();
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
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/discounts")
    public ResponseEntity<Void> deleteDiscountsById(@RequestBody Set<Long> discountIds) {
        discountService.deleteDiscounts(discountIds);
        return ResponseEntity.noContent().build();
    }

}