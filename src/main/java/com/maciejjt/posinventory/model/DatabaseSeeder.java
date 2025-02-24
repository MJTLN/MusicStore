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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Component
@AllArgsConstructor
public class DatabaseSeeder {

    private final ProductRepository productRepository;
   private final ProductService productService;
   private final StorageRepository storageRepository;
   private final DetailFieldRepository detailFieldRepository;
   private final InventoryRepository inventoryRepository;
   private final SupplierRepository supplierRepository;
   private final WarehouseLocationRepository warehouseLocationRepository;
   private final ShipmentRepository shipmentRepository;
   private final SupplierShipmentItemRepository supplierShipmentItemRepository;
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


            WarehouseLocationRequest warehouseLocationRequest1 = WarehouseLocationRequest.builder()
                    .quantity(100)
                    .section(1)
                    .aisle(1)
                    .rack(2)
                    .shelf(1)
                    .position(0)
                    .build();

            WarehouseLocationRequest warehouseLocationRequest2 = WarehouseLocationRequest.builder()
                    .quantity(100)
                    .section(1)
                    .aisle(1)
                    .rack(5)
                    .shelf(2)
                    .position(0)
                    .build();

            WarehouseLocationRequest warehouseLocationRequest3 = WarehouseLocationRequest.builder()
                    .quantity(100)
                    .section(1)
                    .aisle(1)
                    .rack(2)
                    .shelf(2)
                    .position(0)
                    .build();

            WarehouseLocationRequest warehouseLocationRequest4 = WarehouseLocationRequest.builder()
                    .quantity(100)
                    .section(1)
                    .aisle(1)
                    .rack(2)
                    .shelf(3)
                    .position(0)
                    .build();

            WarehouseLocationRequest warehouseLocationRequest5 = WarehouseLocationRequest.builder()
                    .quantity(100)
                    .section(1)
                    .aisle(1)
                    .rack(3)
                    .shelf(1)
                    .position(0)
                    .build();

            WarehouseLocationRequest warehouseLocationRequest6 = WarehouseLocationRequest.builder()
                    .quantity(100)
                    .section(1)
                    .aisle(1)
                    .rack(3)
                    .shelf(2)
                    .position(0)
                    .build();

            WarehouseLocationRequest warehouseLocationRequest7 = WarehouseLocationRequest.builder()
                    .quantity(100)
                    .section(1)
                    .aisle(1)
                    .rack(4)
                    .shelf(1)
                    .position(0)
                    .build();

            WarehouseLocationRequest warehouseLocationRequest8 = WarehouseLocationRequest.builder()
                    .quantity(100)
                    .section(1)
                    .aisle(1)
                    .rack(1)
                    .shelf(1)
                    .position(0)
                    .build();

            WarehouseLocationRequest warehouseLocationRequest9 = WarehouseLocationRequest.builder()
                    .quantity(100)
                    .section(1)
                    .aisle(1)
                    .rack(1)
                    .shelf(2)
                    .position(0)
                    .build();

            WarehouseLocationRequest warehouseLocationRequest10 = WarehouseLocationRequest.builder()
                    .quantity(100)
                    .section(1)
                    .aisle(1)
                    .rack(1)
                    .shelf(3)
                    .position(0)
                    .build();

            WarehouseLocationRequest warehouseLocationRequest11 = WarehouseLocationRequest.builder()
                    .quantity(100)
                    .section(1)
                    .aisle(1)
                    .rack(2)
                    .shelf(1)
                    .position(0)
                    .build();

            WarehouseLocationRequest warehouseLocationRequest12 = WarehouseLocationRequest.builder()
                    .quantity(100)
                    .section(1)
                    .aisle(1)
                    .rack(2)
                    .shelf(2)
                    .position(0)
                    .build();

            WarehouseLocationRequest warehouseLocationRequest13 = WarehouseLocationRequest.builder()
                    .quantity(100)
                    .section(1)
                    .aisle(1)
                    .rack(2)
                    .shelf(3)
                    .position(0)
                    .build();

            inventoryService.addWarehouseLocation(1L,warehouseLocationRequest1);
            inventoryService.addWarehouseLocation(1L,warehouseLocationRequest2);
            inventoryService.addWarehouseLocation(2L,warehouseLocationRequest3);
            inventoryService.addWarehouseLocation(2L,warehouseLocationRequest4);
            inventoryService.addWarehouseLocation(3L,warehouseLocationRequest5);
            inventoryService.addWarehouseLocation(3L,warehouseLocationRequest6);
            inventoryService.addWarehouseLocation(4L,warehouseLocationRequest7);
            inventoryService.addWarehouseLocation(4L,warehouseLocationRequest8);
            inventoryService.addWarehouseLocation(5L,warehouseLocationRequest9);
            inventoryService.addWarehouseLocation(5L,warehouseLocationRequest10);
            inventoryService.addWarehouseLocation(6L,warehouseLocationRequest11);
            inventoryService.addWarehouseLocation(6L,warehouseLocationRequest12);
            inventoryService.addWarehouseLocation(7L,warehouseLocationRequest13);




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
