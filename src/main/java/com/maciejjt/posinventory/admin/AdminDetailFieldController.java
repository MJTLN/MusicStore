package com.maciejjt.posinventory.admin;

import com.maciejjt.posinventory.model.dtos.DetailFieldDto;
import com.maciejjt.posinventory.model.requests.DetailFieldRequest;
import com.maciejjt.posinventory.service.DetailFieldService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@Data
@RequestMapping("/admin/detailField")
public class AdminDetailFieldController {

    private final DetailFieldService detailFieldService;

    @PostMapping
    public ResponseEntity<Long> createDetailField(@RequestBody DetailFieldRequest detailFieldRequest) {
        Long id = detailFieldService.createDetailField(detailFieldRequest).getId();
        URI location = URI.create("/admin/detailField/" + id);
        return ResponseEntity.created(location).build();
    }

    @PutMapping("{detailFieldId}")
    private ResponseEntity<DetailFieldDto> updateFieldValues(@PathVariable Long detailFieldId, @RequestBody List<String> values) {
        return ResponseEntity.ok(detailFieldService.updateFieldValues(detailFieldId, values));
    }

}