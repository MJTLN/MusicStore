package com.maciejjt.posinventory.service;

import com.maciejjt.posinventory.model.DetailField;
import com.maciejjt.posinventory.repository.DetailFieldRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@Data
public class DetailFieldService {

    private final DetailFieldRepository detailFieldRepository;

    public DetailField findById(Long id) {
        return detailFieldRepository.findById(id).orElseThrow();
    }

    public DetailField createDetailField(String name) {
        DetailField detailField = DetailField.builder()
                .name(name)
                .build();
        return detailFieldRepository.save(detailField);
    }
}
