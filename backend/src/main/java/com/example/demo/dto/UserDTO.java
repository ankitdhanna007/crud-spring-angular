package com.example.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
public class UserDTO {

    @NotEmpty(message = "Username is mandatory")
    private String username;

    @NotEmpty(message = "Password is mandatory")
    private String password;

}
