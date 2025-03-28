package com.maciejjt.posinventory.admin;

import com.maciejjt.posinventory.model.api.dtos.PurchaseDto;
import com.maciejjt.posinventory.model.enums.PaymentStatus;
import com.maciejjt.posinventory.model.enums.PurchaseIssueStatus;
import com.maciejjt.posinventory.model.enums.PurchaseStatus;
import com.maciejjt.posinventory.model.enums.ShipmentStatus;
import com.maciejjt.posinventory.model.requests.PurchaseCompletingRequest;
import com.maciejjt.posinventory.service.PurchaseService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/admin/purchase")
@Data
public class AdminPurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping("/complete")
    public ResponseEntity<PurchaseDto> completePurchase(@RequestBody PurchaseCompletingRequest purchaseCompletingRequest) {
        return ResponseEntity.ok(purchaseService.completePurchase(purchaseCompletingRequest));
    }

    @PatchMapping("/{purchaseId}/payment")
    public ResponseEntity<PurchaseDto> updatePaymentStatus(@PathVariable Long purchaseId, @RequestParam PaymentStatus paymentStatus) {
        return ResponseEntity.ok(purchaseService.updatePaymentStatus(purchaseId, paymentStatus));
    }

    @PatchMapping("/{purchaseId}/shipping")
    public ResponseEntity<PurchaseDto> updateShippingStatus(@PathVariable Long purchaseId, @RequestParam ShipmentStatus shipmentStatus) {
        return ResponseEntity.ok(purchaseService.updateShippingStatus(purchaseId, shipmentStatus));
    }

    @PatchMapping("/{purchaseId}/status")
    public ResponseEntity<PurchaseDto> updatePurchaseStatus(@PathVariable Long purchaseId, @RequestParam PurchaseStatus purchaseStatus) {
        return ResponseEntity.ok(purchaseService.updatePurchaseStatus(purchaseId, purchaseStatus));
    }

    @PatchMapping("/issue/{issueId}")
    public ResponseEntity<Void> updateIssueStatus(@PathVariable Long issueId, @RequestParam PurchaseIssueStatus purchaseIssueStatus) {
        purchaseService.updateIssueStatus(issueId, purchaseIssueStatus);
        return ResponseEntity.noContent().build();
    }
}
