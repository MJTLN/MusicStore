package com.maciejjt.posinventory.admin;

import com.maciejjt.posinventory.service.DetailFieldService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Data
@RequestMapping("/admin/detailField")
public class AdminDetailFieldController {

    private final DetailFieldService detailFieldService;

    @PostMapping
    public ResponseEntity<Long> createDetailField(String name) {
        return ResponseEntity.ok(detailFieldService.createDetailField(name).getId());
    }

    //EDIT AND UPDATE PRODUCTS
}