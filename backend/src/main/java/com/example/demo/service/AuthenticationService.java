package com.example.demo.service;

import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.RoleEntity;
import com.example.demo.model.RoleEnum;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            UserRepository userRepository,
            RoleRepository roleRepository, AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public UserEntity authenticate(LoginDTO input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        return userRepository.findByUsername(input.getUsername())
                .orElseThrow();
    }

    public UserEntity signup(UserDTO input, RoleEnum roleEnum) {
        Optional<RoleEntity> optionalRole = roleRepository.findByName(roleEnum);

        if (optionalRole.isEmpty()) {
            return null;
        }

        UserEntity user = UserEntity.builder()
                .username(input.getUsername())
                .password(passwordEncoder.encode(input.getPassword()))
                .role(optionalRole.get())
                .build();

        return userRepository.save(user);
    }

}