package com.cee.business_match_backend.offer.controller;

import com.cee.business_match_backend.auth.model.Role;
import com.cee.business_match_backend.auth.model.User;
import com.cee.business_match_backend.auth.repository.UserRepository;
import com.cee.business_match_backend.offer.model.Offer;
import com.cee.business_match_backend.offer.model.OfferCategory;
import com.cee.business_match_backend.offer.model.OfferStatus;
import com.cee.business_match_backend.offer.repository.OfferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OfferControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        offerRepository.deleteAll();
        userRepository.deleteAll();

        User exporter = userRepository.save(User.builder()
                .email("exporter_filter@test.com")
                .password(passwordEncoder.encode("Test1234!"))
                .role(Role.EXPORTER)
                .fullName("Exporter Filter")
                .companyName("Export Ltd")
                .country("Hungary")
                .description("desc")
                .build());

        offerRepository.save(Offer.builder()
                .title("Hungary Offer")
                .description("desc")
                .category(OfferCategory.EXPORT_EXPANSION)
                .targetRole(Role.INVESTOR)
                .country("Hungary")
                .needsLegalSupport(true)
                .status(OfferStatus.OPEN)
                .creator(exporter)
                .createdAt(LocalDateTime.now())
                .build());

        offerRepository.save(Offer.builder()
                .title("Germany Offer")
                .description("desc")
                .category(OfferCategory.EXPORT_EXPANSION)
                .targetRole(Role.INVESTOR)
                .country("Germany")
                .needsLegalSupport(false)
                .status(OfferStatus.OPEN)
                .creator(exporter)
                .createdAt(LocalDateTime.now())
                .build());

        String response = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "email": "exporter_filter@test.com",
                                  "password": "Test1234!"
                                }
                                """))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        token = com.jayway.jsonpath.JsonPath.read(response, "$.token");
    }

    @Test
    void shouldReturnFilteredOffersWithPagination() throws Exception {
        mockMvc.perform(get("/api/offers")
                        .header("Authorization", "Bearer " + token)
                        .param("page", "0")
                        .param("size", "10")
                        .param("country", "Hungary")
                        .param("targetRole", "INVESTOR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].country").value("Hungary"))
                .andExpect(jsonPath("$.content[0].targetRole").value("INVESTOR"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(10));
    }

}
