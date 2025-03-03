package com.maciejjt.posinventory.service;

import com.maciejjt.posinventory.model.Cart;
import com.maciejjt.posinventory.model.User;
import com.maciejjt.posinventory.model.api.requests.UserRequest;
import com.maciejjt.posinventory.model.enums.Role;
import com.maciejjt.posinventory.repository.CartRepository;
import com.maciejjt.posinventory.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Data
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;

    @Transactional
    public void registerUser(UserRequest userRequest) {

        User user = userRepository.save(
                User.builder()
                        .email(userRequest.getEmail())
                        .password(passwordEncoder.encode(userRequest.getPassword()))
                        .phone(userRequest.getPhone())
                        .role(Role.CUSTOMER)
                        .username(userRequest.getUsername())
                        .build()
        );

        Cart cart = cartRepository.save(Cart.builder()
                .user(user)
                .build()
        );

        user.setCart(cart);
        userRepository.save(user);
    }
}
