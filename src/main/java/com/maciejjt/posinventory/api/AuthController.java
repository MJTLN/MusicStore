package com.maciejjt.posinventory.api;

import com.maciejjt.posinventory.model.api.requests.UserRequest;
import com.maciejjt.posinventory.service.AuthService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Data
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> registerUser(@RequestBody UserRequest userRequest) {
        authService.registerUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}