package com.maciejjt.posinventory.admin;

import com.maciejjt.posinventory.model.dtos.DetailFieldDto;
import com.maciejjt.posinventory.model.requests.DetailFieldRequest;
import com.maciejjt.posinventory.service.DetailFieldService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Data
@RequestMapping("/admin/detailField")
public class AdminDetailFieldController {

    private final DetailFieldService detailFieldService;

    @PostMapping
    public ResponseEntity<Long> createDetailField(@RequestBody DetailFieldRequest detailFieldRequest) {
        return ResponseEntity.ok(detailFieldService.createDetailField(detailFieldRequest).getId());
    }

    @PutMapping("{detailFieldId}")
    private ResponseEntity<DetailFieldDto> updateFieldValues(Long detailFieldId, List<String> values) {
        return ResponseEntity.ok(detailFieldService.updateFieldValues(detailFieldId, values));
    }

}