package com.maciejjt.posinventory.model.api.requests;

import com.maciejjt.posinventory.model.Address;
import com.maciejjt.posinventory.model.Purchase;
import com.maciejjt.posinventory.model.enums.Role;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.Set;

@Data
public class UserRequest {
    private String username;
    private String password;
    private String phone;
    private String email;
}
