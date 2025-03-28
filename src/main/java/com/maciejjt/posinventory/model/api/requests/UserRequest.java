package com.maciejjt.posinventory.model.api.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest {
    @NotNull(message = "You must provide a username")
    @Size(min = 3, max = 100, message = "Username must be between 3 and 30 characters")
    private String username;
    @NotNull(message = "You must provide a password")
    @Size(min = 8, max = 72, message = "Password must be between 8 and 100 characters")
    @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$", message = "Password mus contain at lest one lowercase letter, one uppercase letter, one digit and one special character")
    private String password;
    @NotNull
    @Pattern(regexp = "^[+]?[(]?[0-9]{3}[)]?[-\\s.]?[0-9]{3}[-\\s.]?[0-9]{4,6}$", message = "Incorrect phone number format")
    private String phone;
    @Email(message = "Please supply a valid email address")
    private String email;
}
