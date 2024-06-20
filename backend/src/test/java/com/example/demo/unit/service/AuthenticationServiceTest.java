package com.example.demo.unit.service;

import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.RoleEntity;
import com.example.demo.model.RoleEnum;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateSuccess() {
        LoginDTO loginDTO = new LoginDTO("user", "password");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("user");

        when(userRepository.findByUsername("user")).thenReturn(Optional.of(userEntity));

        UserEntity authenticatedUser = authenticationService.authenticate(loginDTO);

        assertNotNull(authenticatedUser);
        assertEquals("user", authenticatedUser.getUsername());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testAuthenticateFailure() {
        LoginDTO loginDTO = new LoginDTO("user", "password");

        when(userRepository.findByUsername("user")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> authenticationService.authenticate(loginDTO));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testSignupSuccess() {
        UserDTO userDTO = new UserDTO("newuser", "newpassword");
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(RoleEnum.ADMIN);
        UserEntity userEntity = UserEntity.builder()
                .username("newuser")
                .password("encodedPassword")
                .role(roleEntity)
                .build();

        when(roleRepository.findByName(RoleEnum.ADMIN)).thenReturn(Optional.of(roleEntity));
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        UserEntity registeredUser = authenticationService.signup(userDTO, RoleEnum.ADMIN);

        assertNotNull(registeredUser);
        assertEquals("newuser", registeredUser.getUsername());
        assertEquals("encodedPassword", registeredUser.getPassword());
        assertEquals(RoleEnum.ADMIN, registeredUser.getRole().getName());
    }

    @Test
    void testSignupRoleNotFound() {
        UserDTO userDTO = new UserDTO("newuser", "newpassword");

        when(roleRepository.findByName(RoleEnum.EDITOR)).thenReturn(Optional.empty());

        UserEntity registeredUser = authenticationService.signup(userDTO, RoleEnum.EDITOR);

        assertNull(registeredUser);
        verify(userRepository, times(0)).save(any(UserEntity.class));
    }
}
