package com.maciejjt.posinventory.model;

import com.maciejjt.posinventory.model.enums.Role;
import com.maciejjt.posinventory.model.enums.ShipmentStatus;
import com.maciejjt.posinventory.model.requests.*;
import com.maciejjt.posinventory.model.enums.InventoryLocationType;
import com.maciejjt.posinventory.model.enums.ProductLabel;
import com.maciejjt.posinventory.repository.*;
import com.maciejjt.posinventory.service.InventoryService;
import com.maciejjt.posinventory.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Component
@AllArgsConstructor
public class DatabaseSeeder {

    private final ProductRepository productRepository;
   private final ProductService productService;
   private final StorageRepository storageRepository;
   private final DetailFieldRepository detailFieldRepository;
   private final SupplierRepository supplierRepository;
   private final InventoryService inventoryService;
   private final UserRepository userRepository;
   private final PasswordEncoder passwordEncoder;


    @EventListener
    public void seed(ContextRefreshedEvent event) {

        if(!userRepository.existsById(1L)) {
            User user = User.builder()
                    .role(Role.ADMIN)
                    .username("ADMIN")
                    .email("ADMIN@A.A")
                    .phone("123123123")
                    .password(passwordEncoder.encode("123"))
                    .build();

            userRepository.save(user);
        }
        if (!detailFieldRepository.existsById(1L)) {
            DetailField detailField1 = DetailField.builder()
                    .name("pickup configuration")
                    .build();
            DetailField detailField2 = DetailField.builder()
                    .name("wood type")
                    .build();
            DetailField detailField3 = DetailField.builder()
                    .name("number of strings")
                    .build();
            DetailField detailField4 = DetailField.builder()
                    .name("make")
                    .build();
            DetailField detailField5 = DetailField.builder()
                    .name("electronics type")
                    .build();
            detailFieldRepository.save(detailField1);
            detailFieldRepository.save(detailField2);
            detailFieldRepository.save(detailField3);
            detailFieldRepository.save(detailField4);
            detailFieldRepository.save(detailField5);

        }
        if (!productRepository.existsById(1L)) {

            HashSet<Long> categoryIds = new HashSet<>();
            categoryIds.add(0L);

            HashMap<Long, String> productDetails1 = new HashMap<>();
            productDetails1.put(1L, "SH");
            productDetails1.put(2L, "MAPLE");
            productDetails1.put(3L, "6");
            productDetails1.put(4L, "FENDER");
            productDetails1.put(5L, "PASSIVE");

            ProductRequest productRequest1 = ProductRequest.builder()
                    .categoryIds(categoryIds)
                    .UPC(12345678L)
                    .name("Fender Player Telecaster SH BL")
                    .productDetails(productDetails1)
                    .description("new super duper limited edition guitar")
                    .labels(new HashSet<>(Set.of(ProductLabel.NEW,ProductLabel.LIMITED)))
                    .price(450)
                    .build();

            HashMap<Long, String> productDetails2 = new HashMap<>();
            productDetails2.put(1L, "SSS");
            productDetails2.put(2L, "MAPLE");
            productDetails2.put(3L, "6");
            productDetails2.put(4L, "PRS");
            productDetails2.put(5L, "ACTIVE");

            ProductRequest productRequest2 = ProductRequest.builder()
                    .categoryIds(categoryIds)
                    .UPC(12345678L)
                    .name("PRS Silversky Active")
                    .productDetails(productDetails2)
                    .description("nice guitar but a bit expensive")
                    .labels(new HashSet<>(Set.of(ProductLabel.NEW)))
                    .price(800)
                    .build();

            HashMap<Long, String> productDetails3 = new HashMap<>();
            productDetails3.put(1L, "HH");
            productDetails3.put(2L, "MAPLE");
            productDetails3.put(3L, "6");
            productDetails3.put(4L, "SQUIER");
            productDetails3.put(5L, "PASSIVE");

            ProductRequest productRequest3 = ProductRequest.builder()
                    .categoryIds(categoryIds)
                    .UPC(12345678L)
                    .name("Squier Contemproary Jaguar HH CR")
                    .productDetails(productDetails3)
                    .labels(new HashSet<>(Set.of(ProductLabel.DISCOUNT)))
                    .price(1200)
                    .build();

            HashMap<Long, String> productDetails4 = new HashMap<>();
            productDetails4.put(1L, "HS");
            productDetails4.put(2L, "ROSEWOOD");
            productDetails4.put(3L, "6");
            productDetails4.put(4L, "SQUIER");
            productDetails4.put(5L, "ACTIVE");

            ProductRequest productRequest4 = ProductRequest.builder()
                    .categoryIds(categoryIds)
                    .UPC(12345678L)
                    .name("Squier Active Tele SH")
                    .productDetails(productDetails4)
                    .labels(new HashSet<>(Set.of(ProductLabel.DISCOUNT)))
                    .description("this guitar very nice!!!")
                    .price(1000)
                    .build();

            Product product1 = productService.createProduct(productRequest1);

            Product product2 = productService.createProduct(productRequest2);

            Product product3 = productService.createProduct(productRequest3);

            Product product4 = productService.createProduct(productRequest4);

            Storage storageRequest1 = Storage.builder()
                    .type(InventoryLocationType.WAREHOUSE)
                    .address("GDANSK, JANA PAWŁA II 92, 12-123")
                    .build();

            Storage storageRequest2 = Storage.builder()
                    .type(InventoryLocationType.WAREHOUSE)
                    .address("GDYNIA, ŚWIĘTOJANSKA 2, 12-123")
                    .build();

            Storage storageRequest3 = Storage.builder()
                    .type(InventoryLocationType.STORE)
                    .address("GDANSK, GRUNWALDZKA 42, 12-123")
                    .build();

            Storage storageRequest4 = Storage.builder()
                    .type(InventoryLocationType.STORE)
                    .address("SOPOT, MONTE CASSINO 111, 12-123")
                    .build();

            Storage storage1 = storageRepository.save(storageRequest1);
            Storage storage2 = storageRepository.save(storageRequest2);
            Storage storage3 = storageRepository.save(storageRequest3);
            Storage storage4 = storageRepository.save(storageRequest4);

            inventoryService.createInventoryForProduct(product1.getId(),
                    InventoryRequest.builder()
                    .quantity(1000)
                    .storageId(storage1.getId())
                    .build());

            inventoryService.createInventoryForProduct(product2.getId(),
                    InventoryRequest.builder()
                            .quantity(1000)
                            .storageId(storage1.getId())
                            .build());

            inventoryService.createInventoryForProduct(product3.getId(),
                    InventoryRequest.builder()
                            .quantity(1000)
                            .storageId(storage1.getId())
                            .build());

            inventoryService.createInventoryForProduct(product4.getId(),
                    InventoryRequest.builder()
                            .quantity(1000)
                            .storageId(storage1.getId())
                            .build());

            inventoryService.createInventoryForProduct(product1.getId(),
                    InventoryRequest.builder()
                            .quantity(1000)
                            .storageId(storage2.getId())
                            .build());

            inventoryService.createInventoryForProduct(product2.getId(),
                    InventoryRequest.builder()
                            .quantity(1000)
                            .storageId(storage2.getId())
                            .build());

            inventoryService.createInventoryForProduct(product3.getId(),
                    InventoryRequest.builder()
                            .quantity(1000)
                            .storageId(storage2.getId())
                            .build());

            inventoryService.createInventoryForProduct(product4.getId(),
                    InventoryRequest.builder()
                            .quantity(1000)
                            .storageId(storage2.getId())
                            .build());

            inventoryService.createInventoryForProduct(product1.getId(),
                    InventoryRequest.builder()
                            .quantity(1000)
                            .storageId(storage3.getId())
                            .build());

            inventoryService.createInventoryForProduct(product2.getId(),
                    InventoryRequest.builder()
                            .quantity(1000)
                            .storageId(storage3.getId())
                            .build());

            inventoryService.createInventoryForProduct(product3.getId(),
                    InventoryRequest.builder()
                            .quantity(1000)
                            .storageId(storage3.getId())
                            .build());

            inventoryService.createInventoryForProduct(product4.getId(),
                    InventoryRequest.builder()
                            .quantity(1000)
                            .storageId(storage3.getId())
                            .build());

            inventoryService.createInventoryForProduct(product1.getId(),
                    InventoryRequest.builder()
                            .quantity(1000)
                            .storageId(storage4.getId())
                            .build());

            inventoryService.createInventoryForProduct(product2.getId(),
                    InventoryRequest.builder()
                            .quantity(1000)
                            .storageId(storage4.getId())
                            .build());

            inventoryService.createInventoryForProduct(product3.getId(),
                    InventoryRequest.builder()
                            .quantity(1000)
                            .storageId(storage4.getId())
                            .build());


            WarehouseLayoutRequest storage1Request = WarehouseLayoutRequest.builder()
                    .storageId(1L)
                    .layout(Map.of(
                            "S1", Map.of(
                                    "A1", Map.of(
                                            "R1", List.of("S1", "S2", "S3", "S4"),
                                            "R2", List.of("S5", "S6", "S7")
                                    ),
                                    "A2", Map.of(
                                            "R3", List.of("S8", "S9", "S10", "S11"),
                                            "R4", List.of("S12", "S13", "S14")
                                    )
                            ),
                            "S2", Map.of(
                                    "A3", Map.of(
                                            "R5", List.of("S15", "S16", "S17"),
                                            "R6", List.of("S18", "S19")
                                    ),
                                    "A4", Map.of(
                                            "R7", List.of("S20", "S21", "S22"),
                                            "R8", List.of("S23", "S24")
                                    )
                            )
                    ))
                    .build();

            WarehouseLayoutRequest storage2Request = WarehouseLayoutRequest.builder()
                    .storageId(2L)
                    .layout(Map.of(
                            "S1", Map.of(
                                    "A1", Map.of(
                                            "R1", List.of("S1", "S2", "S3", "S4"),
                                            "R2", List.of("S5", "S6", "S7")
                                    ),
                                    "A2", Map.of(
                                            "R3", List.of("S8", "S9", "S10"),
                                            "R4", List.of("S11", "S12")
                                    )
                            ),
                            "S2", Map.of(
                                    "A3", Map.of(
                                            "R5", List.of("S13", "S14", "S15"),
                                            "R6", List.of("S16", "S17", "S18")
                                    ),
                                    "A4", Map.of(
                                            "R7", List.of("S19", "S20", "S21"),
                                            "R8", List.of("S22", "S23", "S24", "S25")
                                    )
                            )
                    ))
                    .build();

            WarehouseLayoutRequest storage3Request = WarehouseLayoutRequest.builder()
                    .storageId(3L)
                    .layout(Map.of(
                            "S1", Map.of(
                                    "A1", Map.of(
                                            "R1", List.of("S1", "S2", "S3"),
                                            "R2", List.of("S4", "S5")
                                    )
                            ),
                            "S2", Map.of(
                                    "A2", Map.of(
                                            "R3", List.of("S6", "S7"),
                                            "R4", List.of("S8", "S9")
                                    )
                            )
                    ))
                    .build();

            WarehouseLayoutRequest storage4Request = WarehouseLayoutRequest.builder()
                    .storageId(4L)
                    .layout(Map.of(
                            "S1", Map.of(
                                    "A1", Map.of(
                                            "R1", List.of("S1", "S2"),
                                            "R2", List.of("S3")
                                    )
                            ),
                            "S2", Map.of(
                                    "A2", Map.of(
                                            "R3", List.of("S4", "S5", "S6")
                                    )
                            )
                    ))
                    .build();

            inventoryService.createWarehouseLayout(storage1Request);
            inventoryService.createWarehouseLayout(storage2Request);
            inventoryService.createWarehouseLayout(storage3Request);
            inventoryService.createWarehouseLayout(storage4Request);

            inventoryService.createWarehouseLayout(storage1Request);
            inventoryService.createWarehouseLayout(storage2Request);
            inventoryService.createWarehouseLayout(storage3Request);
            inventoryService.createWarehouseLayout(storage4Request);

            /*
            inventoryService.putProductsOnRack(product1,);
            inventoryService.putProductsOnRack();
            inventoryService.putProductsOnRack();
            inventoryService.putProductsOnRack();

            inventoryService.putProductsOnRack();
            inventoryService.putProductsOnRack();
            inventoryService.putProductsOnRack();
            inventoryService.putProductsOnRack();

            inventoryService.putProductsOnRack();
            inventoryService.putProductsOnRack();

            inventoryService.putProductsOnRack();
            inventoryService.putProductsOnRack();
*/


            Supplier supplier = Supplier.builder()
                    .companyName("XYZ sp. z.o.o")
                    .officialAddress("Łąkowa 1 Gdańsk 12-123")
                    .contactInfo("123-123-123, mail@mail.com")
                    .build();

           Supplier supplier1 = supplierRepository.save(supplier);

            Set<SupplierShipmentItemRequest> supplierShipmentItems = new HashSet<>();

            supplierShipmentItems.add(SupplierShipmentItemRequest.builder()
                                    .productId(product1.getId())
                                    .quantity(100)
                                    .build());

            supplierShipmentItems.add(SupplierShipmentItemRequest.builder()
                            .productId(product2.getId())
                            .quantity(120)
                            .build());

            supplierShipmentItems.add(SupplierShipmentItemRequest.builder()
                    .productId(product3.getId())
                    .quantity(90)
                    .build());

            supplierShipmentItems.add(SupplierShipmentItemRequest.builder()
                    .productId(product4.getId())
                    .quantity(10)
                    .build());


            SupplierShipmentRequest supplierShipment1 = SupplierShipmentRequest.builder()
                    .amount(new BigDecimal("1000000"))
                    .arrivalTime(LocalDateTime.now().plusDays(10))
                    .status(ShipmentStatus.PLACED)
                    .supplierShipmentItemRequest(supplierShipmentItems)
                    .supplierId(supplier1.getId())
                    .storageId(storage1.getId())
                    .build();

            SupplierShipmentRequest supplierShipment2 = SupplierShipmentRequest.builder()
                    .amount(new BigDecimal("1000000"))
                    .arrivalTime(LocalDateTime.now().plusDays(11))
                    .status(ShipmentStatus.PLACED)
                    .supplierShipmentItemRequest(supplierShipmentItems)
                    .supplierId(supplier1.getId())
                    .storageId(storage2.getId())
                    .build();


            inventoryService.createShipment(supplierShipment1);
            inventoryService.createShipment(supplierShipment2);


        }




    }



}
