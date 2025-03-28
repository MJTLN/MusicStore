package com.maciejjt.posinventory.service;

import com.maciejjt.posinventory.model.*;
import com.maciejjt.posinventory.model.api.dtos.ApiProductDto;
import com.maciejjt.posinventory.model.api.dtos.CartDto;
import com.maciejjt.posinventory.model.api.dtos.PurchaseDto;
import com.maciejjt.posinventory.model.dtos.*;
import com.maciejjt.posinventory.model.enums.ShipmentStatus;
import com.maciejjt.posinventory.model.warehouse.Position;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Data
public class DTOservice {

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

        Set<InventoryDto> inventoryDtos = new HashSet<>();

        product.getInventories().forEach(
                inventoryLocation -> inventoryDtos.add(
                        buildInventoryLocationDto(inventoryLocation))
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
                .inventories(inventoryDtos)
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .description(product.getDescription())
                .descriptionShort(product.getDescriptionShort())
                .build();
    }

    public InventoryDto buildInventoryLocationDto(Inventory inventory) {
        SupplierShipment lastShipment = inventory.getSupplierShipments().stream()
                .filter(shipment -> shipment.getStatus() == ShipmentStatus.COMPLETED)
                .max(Comparator.comparing(SupplierShipment::getArrivalTime))
                .orElse(null);

        SupplierShipment nextShipment = inventory.getSupplierShipments().stream()
                .filter(shipment -> shipment.getStatus() == ShipmentStatus.PLACED || shipment.getStatus() == ShipmentStatus.COMING)
                .min(Comparator.comparing(SupplierShipment::getArrivalTime))
                .orElse(null);

        return InventoryDto.builder()
                .id(inventory.getId())
                .quantity(inventory.getQuantity())
                .storageId(inventory.getStorage().getId())
                .storageId(inventory.getStorage().getId())
                .lastShipment(lastShipment != null ? buildShipmentLastNextDto(lastShipment, inventory.getProduct().getId()) : null)
                .nextShipment(nextShipment != null ? buildShipmentLastNextDto(nextShipment, inventory.getProduct().getId()) : null)
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

        supplierShipment.getSupplierShipmentItems().forEach(item -> supplierShipmentItems.put(
                ProductListingDto.builder()
                        .id(item.getProduct().getId())
                        .label(item.getProduct().getLabel())
                        .name(item.getProduct().getName())
                        .build(),
                item.getQuantity()
        ));

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
            storage.getSupplierShipments().forEach(item -> supplierShipmentWithListings.add(buildShipmentWithListingsDto(item)));
        }

        Set<IInventoryDto> inventoryLocationDtos = new HashSet<>();

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


    private IInventoryDto buildInventoryLocationDtoWithProduct(Inventory inventory) {

        SupplierShipment lastShipment = inventory.getSupplierShipments().stream()
                .filter(shipment -> shipment.getStatus() == ShipmentStatus.COMPLETED)
                .max(Comparator.comparing(SupplierShipment::getArrivalTime))
                .orElse(null);

        SupplierShipment nextShipment = inventory.getSupplierShipments().stream()
                .filter(shipment -> shipment.getStatus() == ShipmentStatus.PLACED || shipment.getStatus() == ShipmentStatus.COMING)
                .min(Comparator.comparing(SupplierShipment::getArrivalTime))
                .orElse(null);



        return InventoryDtoWithProducts.builder()
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
                    .isFixedAmount(discount.isFixedValue())
                    .endDate(discount.getEndDate())
                    .startDate(discount.getStartDate())
                    .saleId(saleId)
                    .productId(discount.getProduct().getId())
                    .amount(discount.getAmount())
                    .build();

    }


    public SaleDto buildSaleDto(Sale sale) {

        Set<DiscountDto> discountDtos = new HashSet<>();

        sale.getDiscounts().forEach(discount -> discountDtos.add(buildDiscountDto(discount)));


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

        category.getChildCategories().forEach(element -> childrenIds.add(element.getId()));

        Set<Long> productIds = new HashSet<>();

        category.getProducts().forEach(product -> productIds.add(product.getId()));

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
        sale.getDiscounts().forEach(discount -> productListingDtos.add(buildProductListingDto(discount.getProduct())));
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
        cart.getProducts().forEach(cartProduct -> products.put(
                buildProductListingDtoShort(cartProduct.getProduct()),
                cartProduct.getQuantity()));
        return CartDto.builder()
                .id(cart.getId())
                .products(products)
                .build();
    }

    public DetailFieldDto buildDetailFieldDto(DetailField detailField) {
        return DetailFieldDto.builder()
                .id(detailField.getId())
                .name(detailField.getName())
                .values(detailField.getValues())
                .build();
    }

    public PositionDto buildPositionDto(Position position) {
        return PositionDto.builder()
                .id(position.getId())
                .shelfId(position.getShelf().getId())
                .number(position.getNumber())
                .quantity(position.getQuantity())
                .build();
    }

    public WarehouseLayoutDto buildWarehouseLayoutDto(WarehouseLayout warehouseLayout) {
        Map<String, Map<String, Map<String, Map<String, List<String>>>>> layout = new HashMap<>();

        warehouseLayout.getSections().forEach(section -> {
            Map<String, Map<String, Map<String, List<String>>>> aisles = new HashMap<>();

            section.getAisles().forEach(aisle -> {
                Map<String, Map<String, List<String>>> racks = new HashMap<>();

                aisle.getRacks().forEach(rack -> {
                    Map<String, List<String>> shelves = new HashMap<>();

                    rack.getShelves().forEach(shelf -> {
                        List<String> positionNumbers = shelf.getPositions().stream()
                                                                .map(Position::getNumber)
                                                                .toList();
                        shelves.put(shelf.getNumber(), positionNumbers);
                    });

                    racks.put(rack.getId() + "," + rack.getNumber(), shelves);
                });

                aisles.put(aisle.getId() + "," + aisle.getNumber(), racks);
            });
            layout.put(section.getId() + "," + section.getNumber(), aisles);
        } );

        return  WarehouseLayoutDto.builder()
                .layout(layout)
                .build();
    }


    public WarehouseLayoutDtoDetailed buildWarehouseLayoutDetailedDto(WarehouseLayout warehouseLayout) {
        Map<String, Map<String, Map<String, Map<String, List<PositionDto>>>>>layout = new HashMap<>();

        warehouseLayout.getSections().forEach(section -> {
            Map<String, Map<String, Map<String, List<PositionDto>>>> aisles = new HashMap<>();

            section.getAisles().forEach(aisle -> {
                Map<String, Map<String, List<PositionDto>>> racks = new HashMap<>();

                aisle.getRacks().forEach(rack -> {
                    Map<String, List<PositionDto>> shelves = new HashMap<>();

                    rack.getShelves().forEach(shelf -> {
                        List<PositionDto> positions = shelf.getPositions().stream()
                                        .map(this::buildPositionDto)
                                        .toList();
                        shelves.put(shelf.getNumber(), positions);
                    });

                    racks.put(rack.getId() + "," + rack.getNumber(), shelves);
                });

                aisles.put(aisle.getId() + "," + aisle.getNumber(), racks);
            });
            layout.put(section.getId() + "," + section.getNumber(), aisles);
        } );

        return WarehouseLayoutDtoDetailed.builder()
                .layout(layout)
                .build();
    }
}
