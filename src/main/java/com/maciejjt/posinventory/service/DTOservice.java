package com.maciejjt.posinventory.service;

import com.maciejjt.posinventory.model.*;
import com.maciejjt.posinventory.model.api.dtos.ApiProductDto;
import com.maciejjt.posinventory.model.api.dtos.CartDto;
import com.maciejjt.posinventory.model.api.dtos.PurchaseDto;
import com.maciejjt.posinventory.model.dtos.*;
import com.maciejjt.posinventory.model.enums.ShipmentStatus;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Data
public class DTOservice {
 /*   public DiscountDto buildDiscountDto(Product product) {

        Sale sale = product.getDiscount().getSale();
        Long saleId = (sale != null) ? sale.getId() : null;

        return  DiscountDto.builder()
                .id(product.getDiscount().getId())
                .isFixedAmount(product.getDiscount().isFixedAmount())
                .saleId(saleId)
                .amount(product.getDiscount().getAmount())
                .name(product.getDiscount().getName())
                .startDate(product.getDiscount().getStartDate())
                .endDate(product.getDiscount().getEndDate())
                .build();
    }*/

    public Set<ProductDetailDto> buildProductDetailDtos(Set<ProductDetail> productDetails) {
        return productDetails.stream()
                .map( productDetail ->
                        ProductDetailDto.builder()
                                .name(productDetail.getDetailField().getName())
                                .value(productDetail.getValue())
                                .build())
                .collect(Collectors.toSet());

    }

    public ProductListingDto buildProductListingDto(Product product) {

        Set<ProductDetailDto> productDetailDtos = buildProductDetailDtos(product.getProductDetails());

        DiscountDto discountDto = null;

        if (product.getDiscount() != null) {
            discountDto = buildDiscountDto(product.getDiscount());
        }

        return ProductListingDto.builder()
                .id(product.getId())
                .label(product.getLabel())
                .name(product.getName())
                .productDetails(productDetailDtos)
                .discount(discountDto)
                .build();
    }

    public ProductDto buildProductDto(Product product) {

        Set<ProductDetailDto> productDetailDtos = buildProductDetailDtos(product.getProductDetails());

        Set<InventoryLocationDto> inventoryLocationDtos = new HashSet<>();

        product.getInventories().forEach(
                inventoryLocation -> {
                    inventoryLocationDtos.add(
                            buildInventoryLocationDto(inventoryLocation));
                }
        );

        int totalQuantity = product.getInventories().stream()
                .mapToInt(Inventory::getQuantity)
                .sum();

        DiscountDto discountDto = null;

        if (product.getDiscount() != null) {
            discountDto = buildDiscountDto(product.getDiscount());
        }

        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .UPC(product.getUPC())
                .price(product.getPrice())
                .productDetails(productDetailDtos)
                .label(product.getLabel())
                .discount(discountDto)
                .totalQuantity(totalQuantity)
                .inventories(inventoryLocationDtos)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .description(product.getDescription())
                .descriptionShort(product.getDescriptionShort())
                .build();
    }

    public InventoryLocationDto buildInventoryLocationDto(Inventory inventory) {
        SupplierShipment lastShipment = inventory.getSupplierShipments().stream()
                .filter(shipment -> shipment.getStatus() == ShipmentStatus.COMPLETED)
                .max(Comparator.comparing(SupplierShipment::getArrivalTime))
                .orElse(null);

        SupplierShipment nextShipment = inventory.getSupplierShipments().stream()
                .filter(shipment -> shipment.getStatus() == ShipmentStatus.PLACED || shipment.getStatus() == ShipmentStatus.COMING)
                .min(Comparator.comparing(SupplierShipment::getArrivalTime))
                .orElse(null);

        Set<WarehouseLocationDto> warehouseLocationDtos = new HashSet<>();

        inventory.getWarehouseLocations().forEach(warehouseLocation -> {
            warehouseLocationDtos.add(buildWarehouseLocationDto(warehouseLocation));
        });

        return InventoryLocationDto.builder()
                .id(inventory.getId())
                .quantity(inventory.getQuantity())
                .storageId(inventory.getStorage().getId())
                .storageId(inventory.getStorage().getId())
                .warehouseLocationDtos(warehouseLocationDtos)
                .lastShipment(lastShipment != null ? buildShipmentLastNextDto(lastShipment, inventory.getProduct().getId()) : null)
                .nextShipment(nextShipment != null ? buildShipmentLastNextDto(nextShipment, inventory.getProduct().getId()) : null)
                .build();
    }

    private WarehouseLocationDto buildWarehouseLocationDto(WarehouseLocation warehouseLocation) {
        return WarehouseLocationDto.builder()
                .id(warehouseLocation.getId())
                .section(warehouseLocation.getSection())
                .aisle(warehouseLocation.getAisle())
                .rack(warehouseLocation.getRack())
                .shelf(warehouseLocation.getShelf())
                .position(warehouseLocation.getPosition())
                .quantity(warehouseLocation.getQuantity())
                .build();
    }

    public StorageBriefDto buildStorageBriefDto(Storage storage) {
        return StorageBriefDto.builder()
                .id(storage.getId())
                .type(storage.getType())
                .address(storage.getAddress())
                .build();
    }

    public SupplierShipmentDto buildShipmentWithListingsDto(SupplierShipment supplierShipment) {

        StorageBriefDto storageBriefDto = buildStorageBriefDto(supplierShipment.getStorage());

        Map<IProductDto, Integer> supplierShipmentItems = new HashMap<>();

        supplierShipment.getSupplierShipmentItems().forEach(item -> {
            supplierShipmentItems.put(
                    ProductListingDto.builder()
                            .id(item.getProduct().getId())
                            .label(item.getProduct().getLabel())
                            .name(item.getProduct().getName())
                            .discount(null) // Co to kurwa jest
                            .productDetails(null)
                            .build(),
                    item.getQuantity()
            );
        });

        SupplierDtoWithShipments supplier = SupplierDtoWithShipments.builder()
                .id(supplierShipment.getSupplier().getId())
                .companyName(supplierShipment.getSupplier().getCompanyName())
                .officialAddress(supplierShipment.getSupplier().getOfficialAddress())
                .contactInfo(supplierShipment.getSupplier().getContactInfo())
                .build();


        return SupplierShipmentDto.builder()
                .id(supplierShipment.getId())
                .note(supplierShipment.getNote())
                .status(supplierShipment.getStatus())
                .createdAt(supplierShipment.getCreatedAt())
                .arrivalTime(supplierShipment.getArrivalTime())
                .amount(supplierShipment.getAmount())
                .storage(storageBriefDto)
                .supplierShipmentItems(supplierShipmentItems)
                .supplier(supplier)
                .build();
    }


    public StorageDto buildStorageDto(Storage storage, boolean withProducts) {

        Set<SupplierShipmentDto> supplierShipmentWithListings = new HashSet<>();

        if (storage.getSupplierShipments() != null) {
            storage.getSupplierShipments().forEach(item -> {
                supplierShipmentWithListings.add(buildShipmentWithListingsDto(item));
            });
        }

        Set<IinventoryLocationDto> inventoryLocationDtos = new HashSet<>();

        if (storage.getInventories() != null) {
            storage.getInventories().forEach(item -> {
                if (withProducts) {
                    inventoryLocationDtos.add(buildInventoryLocationDtoWithProduct(item));
                } else {
                    inventoryLocationDtos.add(buildInventoryLocationDto(item));
                }
            });
        }

        return StorageDto.builder()
                .id(storage.getId())
                .type(storage.getType())
                .address(storage.getAddress())
                .supplierShipments(supplierShipmentWithListings)
                .inventoryLocations(inventoryLocationDtos)
                .build();
    }


    private IinventoryLocationDto buildInventoryLocationDtoWithProduct(Inventory inventory) {

        /*
        SupplierShipment lastShipment = null;
        SupplierShipment nextShipment = null;


        for (SupplierShipment supplierShipment : inventory.getSupplierShipments()) {
            if (supplierShipment.getStatus() == ShipmentStatus.COMPLETED) {
                if (lastShipment == null || lastShipment.getArrivalTime().isBefore(supplierShipment.getArrivalTime())) {
                        lastShipment = supplierShipment;
                }
            }
            if (supplierShipment.getStatus() == ShipmentStatus.PLACED || supplierShipment.getStatus() == ShipmentStatus.COMING) {
                if (supplierShipment == null || nextShipment.getArrivalTime().isAfter(supplierShipment.getArrivalTime())) {
                    nextShipment = supplierShipment;
                }
            }
        }
         */

        SupplierShipment lastShipment = inventory.getSupplierShipments().stream()
                .filter(shipment -> shipment.getStatus() == ShipmentStatus.COMPLETED)
                .max(Comparator.comparing(SupplierShipment::getArrivalTime))
                .orElse(null);

        SupplierShipment nextShipment = inventory.getSupplierShipments().stream()
                .filter(shipment -> shipment.getStatus() == ShipmentStatus.PLACED || shipment.getStatus() == ShipmentStatus.COMING)
                .min(Comparator.comparing(SupplierShipment::getArrivalTime))
                .orElse(null);



        return InventoryLocationDtoWithProducts.builder()
                .id(inventory.getId())
                .quantity(inventory.getQuantity())
                .productListingDto(buildProductListingDtoShort(inventory.getProduct()))
                .lastShipment(lastShipment != null ? buildShipmentLastNextDto(lastShipment, inventory.getProduct().getId()) : null)
                .nextShipment(nextShipment != null ? buildShipmentLastNextDto(nextShipment, inventory.getProduct().getId()) : null)
                .build();
    }

    public SupplierShipmentLastNextDto buildShipmentLastNextDto(SupplierShipment supplierShipment, Long productId) {
        SupplierShipmentItem supplierShipmentItem = supplierShipment.getSupplierShipmentItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        return SupplierShipmentLastNextDto.builder()
                .id(supplierShipment.getId())
                .arrivalTime(supplierShipment.getArrivalTime())
                .quantity(supplierShipmentItem.getQuantity())
                .build();
    }

    private ProductListingDtoShort buildProductListingDtoShort(Product product) {
        return ProductListingDtoShort.builder()
                .id(product.getId())
                .name(product.getName())
                .label(product.getLabel())
                .build();
    }

    public DiscountDto buildDiscountDto(Discount discount) {
        Long saleId = Optional.ofNullable(discount.getSale())
                .map(Sale::getId)
                .orElse(null);

            return DiscountDto.builder()
                    .name(discount.getName())
                    .id(discount.getId())
                    .isFixedAmount(discount.isFixedAmount())
                    .endDate(discount.getEndDate())
                    .startDate(discount.getStartDate())
                    .saleId(saleId)
                    .productId(discount.getProduct().getId())
                    .amount(discount.getAmount())
                    .build();

    }


    public SaleDto buildSaleDto(Sale sale) {

        Set<DiscountDto> discountDtos = new HashSet<>();

        sale.getDiscounts().forEach(discount -> {
            discountDtos.add(buildDiscountDto(discount));
        });


        return SaleDto.builder()
                .id(sale.getId())
                .name(sale.getName())
                .description(sale.getDescription())
                .discounts(discountDtos)
                .type(sale.getIsAggregating())
                .startDate(sale.getStartDate())
                .endDate(sale.getEndDate())
                .build();
    }

    public CategoryDto buildCategoryDto(Category category) {

        Set<Long> childrenIds = new HashSet<>();

        category.getChildCategories().forEach(element -> {
            childrenIds.add(element.getId());
        });

        Set<Long> productIds = new HashSet<>();

        category.getProducts().forEach(product -> {
            productIds.add(product.getId());
        });

        Long parentId = Optional.ofNullable(category.getParentCategory())
                .map(Category::getId)
                .orElse(null);

        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .parentId(parentId)
                .childrenIds(childrenIds)
                .productIds(productIds)
                .build();
    }

    public SaleDtoWithProducts buildSaleDtoWithProducts(Sale sale) {
        Set<ProductListingDto> productListingDtos = new HashSet<>();
        sale.getDiscounts().forEach(discount -> {
            productListingDtos.add(buildProductListingDto(discount.getProduct()));
        });
        return SaleDtoWithProducts.builder()
                .description(sale.getDescription())
                .name(sale.getName())
                .id(sale.getId())
                .products(productListingDtos)
                .type(sale.getIsAggregating())
                .startDate(sale.getStartDate())
                .endDate(sale.getEndDate())
                .build();
    }


    public ApiProductDto buildApiProductDto(Product product) {
        return ApiProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .UPC(product.getUPC())
                .description(product.getDescription())
                .descriptionShort(product.getDescriptionShort())
                .discount(buildDiscountDto(product.getDiscount()))
                .productDetails(buildProductDetailDtos(product.getProductDetails()))
                .label(product.getLabel())
                .build();
    }

    public SupplierDtoWithShipments buildSupplierDtoWithShipments(Supplier supplier) {

        Set<SupplierShipmentDto> supplierShipments = new HashSet<>();

        if (supplier.getShipments() != null) {
            supplierShipments = supplier.getShipments().stream()
                    .map(this::buildShipmentWithListingsDto)
                    .collect(Collectors.toSet());
        }

        return SupplierDtoWithShipments.builder()
                .id(supplier.getId())
                .companyName(supplier.getCompanyName())
                .officialAddress(supplier.getOfficialAddress())
                .contactInfo(supplier.getContactInfo())
                .supplierShipments(supplierShipments)
                .build();
    }

    public PurchaseDto buildPurchaseDto(Purchase purchase) {

        Set<ProductListingDtoShort> productListingDtos = purchase.getProducts().stream()
                .map(this::buildProductListingDtoShort)
                .collect(Collectors.toSet());

        return PurchaseDto.builder()
                .amount(purchase.getAmount())
                .products(productListingDtos)
                .paymentStatus(purchase.getPayment().getPaymentStatus())
                .shipmentStatus(purchase.getShipmentStatus())
                .purchaseStatus(purchase.getPurchaseStatus())
                .mail(purchase.getMail())
                .phone(purchase.getPhone())
                .country(purchase.getCountry())
                .city(purchase.getCity())
                .street(purchase.getStreet())
                .house(purchase.getHouse())
                .apartment(purchase.getApartment())
                .createdAt(purchase.getCreatedAt())
                .updatedAt(purchase.getUpdatedAt())
                .build();

    }

    public SupplierDto buildSupplierDto(Supplier supplier) {

        return SupplierDto.builder()
                .id(supplier.getId())
                .companyName(supplier.getCompanyName())
                .officialAddress(supplier.getOfficialAddress())
                .contactInfo(supplier.getContactInfo())
                .build();
    }

    public StorageMovementItemDto buildStorageMovementItemDto(StorageMovementItem item) {
        return StorageMovementItemDto.builder()
                .product(buildProductListingDto(item.getProduct()))
                .quantity(item.getQuantity())
                .accepted(item.isAccepted())
                .build();
    }

    public StorageMovementDto buildStorageMovementDto(StorageMovement storageMovement) {

        Set<StorageMovementItemDto> storageMovementItemDtos = storageMovement.getMovementItems().stream()
                .map(this::buildStorageMovementItemDto)
                .collect(Collectors.toSet());

        return StorageMovementDto.builder()
                .id(storageMovement.getId())
                .currentStorage(buildStorageBriefDto(storageMovement.getCurrentStorage()))
                .newStorage(buildStorageBriefDto(storageMovement.getNewStorage()))
                .status(storageMovement.getStatus())
                .movementItems(storageMovementItemDtos)
                .note(storageMovement.getNote())
                .build();
    }

    public CartDto buildCartDto(Cart cart) {
        Map<ProductListingDtoShort, Integer> products = new HashMap<>();
        cart.getProducts().forEach(cartProduct -> {
            products.put(
                    buildProductListingDtoShort(cartProduct.getProduct()),
                    cartProduct.getQuantity());
        });
        return CartDto.builder()
                .id(cart.getId())
                .products(products)
                .build();
    }
}
