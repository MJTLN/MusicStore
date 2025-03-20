package com.maciejjt.posinventory.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.maciejjt.posinventory.exceptions.EntityNotFoundException;
import com.maciejjt.posinventory.model.Category;
import com.maciejjt.posinventory.model.DetailField;
import com.maciejjt.posinventory.model.dtos.DetailFieldDto;
import com.maciejjt.posinventory.model.requests.DetailFieldRequest;
import com.maciejjt.posinventory.model.specifications.DetailFieldSpecification;
import com.maciejjt.posinventory.repository.CategoryRepository;
import com.maciejjt.posinventory.repository.DetailFieldRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
public class DetailFieldService {

    private final DetailFieldRepository detailFieldRepository;
    private final DTOservice dtOservice;
    private final CategoryRepository categoryRepository;

    public DetailField findById(Long id) {
        return detailFieldRepository.findById(id).orElseThrow();
    }

    @Transactional
    public DetailField createDetailField(DetailFieldRequest request) {
        List<Category> categories = new ArrayList<>();
        if (request.getCategoryIds() != null) {
           categories = categoryRepository.findAllById(request.getCategoryIds());
        }
        DetailField detailField = DetailField.builder()
                .name(request.getName())
                .categories(categories)
                .build();
        return detailFieldRepository.save(detailField);
    }

    public List<DetailFieldDto> findDetailFieldByCategory(Long categoryId) {
        Specification<DetailField> specification = DetailFieldSpecification.filterByCategory(categoryId);
        List<DetailField> detailFields = detailFieldRepository.findAll(specification);
        return detailFields.stream()
                .map(dtOservice::buildDetailFieldDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public DetailFieldDto updateFieldValues(Long id, List<String> values) {
        DetailField detailField = detailFieldRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("detail field not found with id " + id));
        try {
            detailField.setValuesList(values);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("error");
        }
        return dtOservice.buildDetailFieldDto(detailFieldRepository.save(detailField));
    }
}
