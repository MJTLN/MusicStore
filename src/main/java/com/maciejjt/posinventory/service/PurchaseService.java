package com.maciejjt.posinventory.service;

import com.maciejjt.posinventory.exceptions.EntityNotFoundException;
import com.maciejjt.posinventory.exceptions.WarehouseConflictException;
import com.maciejjt.posinventory.model.*;
import com.maciejjt.posinventory.model.api.dtos.PurchaseDto;
import com.maciejjt.posinventory.model.api.requests.PurchaseIssueRequest;
import com.maciejjt.posinventory.model.api.requests.PurchaseRequest;
import com.maciejjt.posinventory.model.enums.PaymentStatus;
import com.maciejjt.posinventory.model.enums.PurchaseIssueStatus;
import com.maciejjt.posinventory.model.enums.PurchaseStatus;
import com.maciejjt.posinventory.model.enums.ShipmentStatus;
import com.maciejjt.posinventory.model.requests.PurchaseCompletingRequest;
import com.maciejjt.posinventory.model.warehouse.Position;
import com.maciejjt.posinventory.repository.*;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Data
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final InventoryRepository inventoryRepository;
    private final DTOservice dtOservice;
    private final PurchaseIssueRepository purchaseIssueRepository;
    private final PositionRepository positionRepository;

    @Transactional
    public Purchase createPurchase(PurchaseRequest purchaseRequest, User user) {

        Set<Product> products =  new HashSet<>(productRepository.findAllById(purchaseRequest.getProductIds()));

        BigDecimal amount = products.stream()
                .map( product -> {
                    BigDecimal price = product.getPrice();
                    if (product.getDiscount() != null && product.getDiscount().isActive()) {
                        if (product.getDiscount().isFixedValue()) {
                            price = price.subtract(new BigDecimal(product.getDiscount().getAmount()));
                        } else {
                            BigDecimal multiplicator = BigDecimal.ONE.subtract(new BigDecimal(product.getDiscount().getAmount()));
                            price = price.multiply(multiplicator);
                        }
                    }
                    return price;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        Purchase purchase = purchaseRepository.save(Purchase.builder()
                .purchaseStatus(PurchaseStatus.COMPLETING)
                .shipmentStatus(ShipmentStatus.NOT_PLACED)
                .amount(amount)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .shipper(purchaseRequest.getShipper())
                .user(user)
                .country(purchaseRequest.getCountry())
                .city(purchaseRequest.getCity())
                .street(purchaseRequest.getStreet())
                .house(purchaseRequest.getHouse())
                .apartment(purchaseRequest.getApartment())
                .phone(purchaseRequest.getPhone())
                .mail(purchaseRequest.getMail())
                .products(products)
                .build());

        Payment payment = paymentRepository.save(Payment.builder()
                .paymentStatus(PaymentStatus.TOBEPAYED)
                .purchase(purchase)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user)
                .build());

        purchase.setPayment(payment);

        return purchaseRepository.save(purchase);
    }

    @Transactional
    public PurchaseDto updatePaymentStatus(Long purchaseId, PaymentStatus paymentStatus) {
        Purchase purchase = findPurchaseById(purchaseId);
        Payment payment = purchase.getPayment();
        payment.setPaymentStatus(paymentStatus);
        paymentRepository.save(payment);

        return dtOservice.buildPurchaseDto(purchase);
    }

    @Transactional
    public PurchaseDto updatePurchaseStatus(Long purchaseId, PurchaseStatus purchaseStatus) {
        Purchase purchase = findPurchaseById(purchaseId);
        purchase.setPurchaseStatus(purchaseStatus);

        return dtOservice.buildPurchaseDto(purchaseRepository.save(purchase));
    }

    @Transactional
    public PurchaseDto updateShippingStatus(Long purchaseId, ShipmentStatus shipmentStatus) {
        Purchase purchase = findPurchaseById(purchaseId);
        purchase.setShipmentStatus(shipmentStatus);

        return dtOservice.buildPurchaseDto(purchaseRepository.save(purchase));
    }

    public PurchaseDto getPurchaseById(Long purchaseId) {
        return dtOservice.buildPurchaseDto(findPurchaseById(purchaseId));
    }

    @Transactional
    public PurchaseDto completePurchase(PurchaseCompletingRequest purchaseCompletingRequest) {
        Purchase purchase = findPurchaseById(purchaseCompletingRequest.getPurchaseId());

        purchaseCompletingRequest.getProductStock().forEach((key,value) -> {
           Position position = positionRepository.findById(value.getPositionId())
                    .orElseThrow(() -> new EntityNotFoundException("Position not found with id " + value.getPositionId()));
            if (key.equals(position.getInventory().getProduct().getId())) {
                if (position.getQuantity() < value.getAmount()) {
                    throw new RuntimeException("Not enough quantity in this position");
                }
                position.removeQuantity(value.getAmount());
                if (position.getQuantity() == 0) {
                    position.setInventory(null);
                }
                Inventory inventory = position.getInventory();
                inventory.removeQuantity(value.getAmount());

                positionRepository.save(position);
                inventoryRepository.save(inventory);
            } else {
                throw new WarehouseConflictException("Provided warehouse position contains other product than specified.");
            }
        });

        purchase.setPurchaseStatus(PurchaseStatus.READY_FOR_SHIPPING);

        return dtOservice.buildPurchaseDto(purchaseRepository.save(purchase));
    }

    @Transactional
    public PurchaseIssue createIssueForPurchase(Long purchaseId, PurchaseIssueRequest purchaseIssueRequest) {
        Purchase purchase = findPurchaseById(purchaseId);
        PurchaseIssue purchaseIssue = purchaseIssueRepository.save(
                PurchaseIssue.builder()
                .description(purchaseIssueRequest.description)
                .status(PurchaseIssueStatus.NEW)
                .purchase(purchase)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build()
        );

        purchase.setPurchaseIssue(purchaseIssue);
        purchaseRepository.save(purchase);

        return purchaseIssue;
    }

    @Transactional
    public void updateIssueStatus(Long issueId, PurchaseIssueStatus purchaseIssueStatus) {
        PurchaseIssue purchaseIssue = findPurchaseIssueById(issueId);
        purchaseIssue.setStatus(purchaseIssueStatus);
        purchaseIssueRepository.save(purchaseIssue);
    }

    public Purchase findPurchaseById(Long id) {
        return purchaseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Purchase not found with id " + id));
    }

    public PurchaseIssue findPurchaseIssueById(Long id) {
        return purchaseIssueRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Issue not found with id " + id));
    }

    public Set<PurchaseDto> getUserPurchases(User user) {
        return user.getPurchases().stream()
                .map(dtOservice::buildPurchaseDto)
                .collect(Collectors.toSet());
    }
}
