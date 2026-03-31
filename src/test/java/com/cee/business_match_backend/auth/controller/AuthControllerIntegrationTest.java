package com.cee.business_match_backend.auth.controller;

import com.cee.business_match_backend.auth.model.Role;
import com.cee.business_match_backend.auth.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterAndLoginUserSuccessfully() throws Exception {
        Map<String, Object> registerRequest = Map.of(
                "email", "investor_test@test.com",
                "password", "Test1234!",
                "role", Role.INVESTOR.name(),
                "fullName", "Investor Test",
                "companyName", "Test Capital",
                "country", "Hungary",
                "description", "Test investor"
        );

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("investor_test@test.com"))
                .andExpect(jsonPath("$.role").value("INVESTOR"))
                .andExpect(jsonPath("$.token").isNotEmpty());

        assertThat(userRepository.findByEmail("investor_test@test.com")).isPresent();

        Map<String, Object> loginRequest = Map.of(
                "email", "investor_test@test.com",
                "password", "Test1234!"
        );

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("investor_test@test.com"))
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

}
