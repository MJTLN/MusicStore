package com.maciejjt.posinventory.model.requests;

import lombok.Data;

import java.util.List;

@Data
public class DetailFieldRequest {
    String name;
    List<Long> categoryIds;
}
