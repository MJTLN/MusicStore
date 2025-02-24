package com.maciejjt.posinventory.api;

import com.maciejjt.posinventory.model.Purchase;
import com.maciejjt.posinventory.model.User;
import com.maciejjt.posinventory.model.api.dtos.PurchaseDto;
import com.maciejjt.posinventory.model.api.requests.PurchaseIssueRequest;
import com.maciejjt.posinventory.model.api.requests.PurchaseRequest;
import com.maciejjt.posinventory.repository.UserRepository;
import com.maciejjt.posinventory.service.CustomUserDetails;
import com.maciejjt.posinventory.service.PurchaseService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@Data
@RequestMapping("/purchase")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final UserRepository userRepository;

    @PostMapping()
    public ResponseEntity<PurchaseDto> createPurchase(@RequestBody PurchaseRequest purchaseRequest, Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        User user = userRepository.findUserByEmail(userDetails.getUsername()) // Now fetching by email
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(purchaseService.createPurchase(purchaseRequest, user));
    }

    @GetMapping("/{purchaseId}")
    public ResponseEntity<PurchaseDto> getPurchaseById(@PathVariable Long purchaseId) {
        return ResponseEntity.ok(purchaseService.getPurchaseById(purchaseId));
    }

    @PostMapping("/issue/{purchaseId}")
    public ResponseEntity<Void> createIssueForPurchase(@PathVariable Long purchaseId, @RequestBody PurchaseIssueRequest purchaseIssueRequest) {
        purchaseService.createIssueForPurchase(purchaseId, purchaseIssueRequest);
        return ResponseEntity.ok().build();
    }

}
