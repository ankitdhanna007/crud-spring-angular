package com.example.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@AllArgsConstructor
public class LoginDTO {

    @NotEmpty(message = "Username is mandatory")
    private String username;

    @NotEmpty(message = "Password is mandatory")
    private String password;

}
