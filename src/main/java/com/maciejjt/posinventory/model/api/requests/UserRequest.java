package com.maciejjt.posinventory.model.api.requests;

import lombok.Data;

@Data
public class UserRequest {
    private String username;
    private String password;
    private String phone;
    private String email;
}
