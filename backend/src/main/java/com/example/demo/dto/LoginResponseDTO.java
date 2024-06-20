package com.example.demo.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDTO {

    private String token;
    private long expiresIn;
    private String username;
    private String role;
}
