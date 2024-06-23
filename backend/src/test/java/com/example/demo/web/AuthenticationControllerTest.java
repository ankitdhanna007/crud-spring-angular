package com.example.demo.web;

import com.example.demo.controller.AuthenticationController;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.model.RoleEntity;
import com.example.demo.model.UserEntity;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static com.example.demo.model.RoleEnum.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(AuthenticationController.class)
@WebAppConfiguration
public class AuthenticationControllerTest {

    protected MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationService authenticationService;

    @Autowired
    private WebApplicationContext context;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testMissingUsername() throws Exception {

        final String inputJson = this.mapToJson(new UserDTO(null, null));
        MvcResult mvcResult = mockMvc.perform(
                        post("/auth/signup")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(inputJson))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("Username is mandatory"));
    }

    @Test
    public void testMissingUsernameAndPassword() throws Exception {

        final String inputJson = this.mapToJson(new UserDTO(null, null));
        MvcResult mvcResult = mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(inputJson))
                .andDo(print())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        assertTrue(content.contains("Username is mandatory"));
        assertTrue(content.contains("Password is mandatory"));

    }

    @Test
    public void testLoginSuccessful() throws Exception {

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("testUser");
        userEntity.setPassword("testUser");
        userEntity.setRole(new RoleEntity(ADMIN, null));

        Mockito.when(authenticationService.authenticate(any(LoginDTO.class))).thenReturn(userEntity);
        Mockito.when(jwtService.generateToken(any(UserDetails.class))).thenReturn("JWT_TOKEN");

        final String inputJson = this.mapToJson(new UserDTO("USERNAME", "PASSWORD"));

        MvcResult mvcResult = mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(inputJson))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        LoginResponseDTO loginResponseDTO = mapToObject(content, LoginResponseDTO.class);
        assertEquals("JWT_TOKEN", loginResponseDTO.getToken());
        assertEquals("testUser", loginResponseDTO.getUsername());
        assertEquals(ADMIN.name(), loginResponseDTO.getRole());
    }


    private String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    private <T> T mapToObject(String jsonString, Class<T> clazz) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonString, clazz);
    }

}
