package com.maciejjt.posinventory.service;

import com.maciejjt.posinventory.exceptions.DeletionException;
import com.maciejjt.posinventory.exceptions.EntityNotFoundException;
import com.maciejjt.posinventory.model.Supplier;
import com.maciejjt.posinventory.model.dtos.SupplierDto;
import com.maciejjt.posinventory.model.dtos.SupplierDtoWithShipments;
import com.maciejjt.posinventory.model.requests.SupplierRequest;
import com.maciejjt.posinventory.repository.SupplierRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@Data
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final DTOservice dtoService;

    @Transactional
    public Supplier createSupplier(SupplierRequest request ) {
        Supplier supplier = Supplier.builder()
                .companyName(request.getCompanyName())
                .contactInfo(request.getContactInfo())
                .officialAddress(request.getOfficialAddress())
                .build();

        return supplierRepository.save(supplier);
    }

    @Transactional
    public SupplierDto updateSupplier(Long supplierId, SupplierRequest supplierRequest) {
            Supplier supplier = findSupplierById(supplierId);
            BeanUtils.copyProperties(supplierRequest,supplier);
            return dtoService.buildSupplierDto(supplierRepository.save(supplier));
    }

    public SupplierDtoWithShipments findSupplierWithShipmentsById(Long supplierId) {
        Supplier supplier =  supplierRepository.findSupplierWShipmentsById(supplierId)
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found with id " + supplierId));
        return dtoService.buildSupplierDtoWithShipments(supplier);
    }

    @Transactional
    public void deleteSupplierById(Long supplierId) {
        Supplier supplier = findSupplierById(supplierId);
        if (!supplier.getShipments().isEmpty()) {
            throw new DeletionException("Supplier's shipments need to be deleted before deleting supplier");
        } else {
            supplierRepository.delete(supplier);
        }
    }

    public SupplierDto getSupplierById(Long supplierId) {
        Supplier supplier = findSupplierById(supplierId);
        return dtoService.buildSupplierDto(supplier);
    }
    
    private Supplier findSupplierById(Long id) {
        return supplierRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Supplier not found with id: " + id));
    }
}
