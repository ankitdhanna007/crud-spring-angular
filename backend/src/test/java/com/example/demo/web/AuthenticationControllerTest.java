package com.example.demo.web;

import com.example.demo.config.SecurityConfiguration;
import com.example.demo.controller.AuthenticationController;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.RoleEntity;
import com.example.demo.model.RoleEnum;
import com.example.demo.model.UserEntity;
import com.example.demo.security.JwtAuthenticationFilter;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@ContextConfiguration(classes=SecurityConfiguration.class)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private JwtService jwtService;

    //@Test  //todo - Test not working correctly. mockMvc always return OK
    void testRegister() throws Exception {

        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(RoleEnum.VIEWER);
        UserEntity userEntity = UserEntity.builder()
                .username("newuser")
                .password("newpassword")
                .role(roleEntity)
                .build();

        when(authenticationService.signup(any(UserDTO.class), any(RoleEnum.class))).thenReturn(userEntity);

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"password\":\"newpassword\"}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

   // @Test //todo - Test not working correctly. mockMvc always return OK
    void testAuthenticate() throws Exception {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(RoleEnum.VIEWER);
        UserEntity userEntity = UserEntity.builder()
                .username("user")
                .password("password")
                .role(roleEntity)
                .build();
        String token = "jwt-token";

        when(authenticationService.authenticate(any(LoginDTO.class))).thenReturn(userEntity);
        when(jwtService.generateToken(any(UserEntity.class))).thenReturn(token);
        when(jwtService.getExpirationTime()).thenReturn(3600L);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"user\",\"password\":\"password\"}"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}