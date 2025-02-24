package com.maciejjt.posinventory.model;

import com.maciejjt.posinventory.model.enums.PurchaseIssueStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseIssue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String description;
    @OneToOne
    @JoinColumn(name = "PURCHASE_ID", referencedColumnName = "ID")
    public Purchase purchase;
    public LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public PurchaseIssueStatus status;
}
