package com.maciejjt.posinventory.model;

    import jakarta.persistence.*;
    import lombok.*;

    import java.util.Objects;

    @Entity
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(exclude = "product")
    public class ProductDetail {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @ManyToOne
        @JoinColumn(name = "PRODUCT_ID", referencedColumnName = "ID")
        private Product product;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "FIELD_ID", referencedColumnName = "ID")
        private DetailField detailField;

        private String name;
        private String value;

    }
