package com.example.demo.controller;

import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.RoleEnum;
import com.example.demo.model.UserEntity;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Rest API controller to consume sign up and login requests
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthenticationController {

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserEntity> register(@Valid @RequestBody UserDTO registerUserDto) {
        UserEntity registeredUser = authenticationService.signup(registerUserDto, RoleEnum.VIEWER);
        return ResponseEntity.ok(registeredUser);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> authenticate(@Valid @RequestBody LoginDTO loginUserDto) {
        UserEntity authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(jwtToken, jwtService.getExpirationTime(),
                authenticatedUser.getUsername(),
                authenticatedUser.getRole().getName().name());

        return ResponseEntity.ok(loginResponseDTO);
    }

}
