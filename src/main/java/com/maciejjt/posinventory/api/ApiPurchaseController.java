package com.maciejjt.posinventory.api;

import com.maciejjt.posinventory.exceptions.AuthenticationException;
import com.maciejjt.posinventory.model.User;
import com.maciejjt.posinventory.model.api.dtos.PurchaseDto;
import com.maciejjt.posinventory.model.api.requests.PurchaseIssueRequest;
import com.maciejjt.posinventory.model.api.requests.PurchaseRequest;
import com.maciejjt.posinventory.repository.UserRepository;
import com.maciejjt.posinventory.service.AuthService;
import com.maciejjt.posinventory.service.CustomUserDetails;
import com.maciejjt.posinventory.service.PurchaseService;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@Data
@RequestMapping("/purchase")
public class ApiPurchaseController {

    private final PurchaseService purchaseService;
    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping()
    public ResponseEntity<PurchaseDto> createPurchase(@RequestBody PurchaseRequest purchaseRequest, Authentication authentication) {
        User user = findUser(authentication);
        Long id = purchaseService.createPurchase(purchaseRequest, user).getId();
        URI location = URI.create("/purchase/" + id);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{purchaseId}")
    public ResponseEntity<PurchaseDto> getPurchaseById(@PathVariable Long purchaseId) {
        return ResponseEntity.ok(purchaseService.getPurchaseById(purchaseId));
    }

    @PostMapping("/issue/{purchaseId}")
    public ResponseEntity<Void> createIssueForPurchase(@PathVariable Long purchaseId, @RequestBody PurchaseIssueRequest purchaseIssueRequest) {
        Long id = purchaseService.createIssueForPurchase(purchaseId, purchaseIssueRequest).getId();
        URI location = URI.create("/purchase/issue/" + id);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/history")
    private ResponseEntity<Page<PurchaseDto>> getUserPurchases(Authentication authentication,
                                                               @RequestParam int page,
                                                               @RequestParam int size) {
        User user = findUser(authentication);
        return ResponseEntity.ok(purchaseService.getUserPurchases(user, page, size));
    }

    private User findUser(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userRepository.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new AuthenticationException("There was a error authenticating the user"));
    }
}