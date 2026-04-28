package com.pineaple.pineaple.products.controllers;

import com.pineaple.pineaple.products.repository.ProductRepository;
import com.pineaple.pineaple.shared.security.JwtService;
import com.pineaple.pineaple.users.entities.User;
import com.pineaple.pineaple.users.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductsControllerSecurityIntegrationTest {

    private static final String PRODUCT_PAYLOAD = """
            {
              \"name\": \"Pineapple T-Shirt\",
              \"description\": \"Fresh tropical shirt\",
              \"price\": 29.99,
              \"currency\": \"USD\"
            }
            """;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void createProduct_withValidBearerToken_returnsCreatedAndPersists() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = User.create(userId, "john.doe@example.com", "hashed-password");
        userRepository.save(user);

        String token = jwtService.generateAccessToken(userId, user.getEmail());

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(PRODUCT_PAYLOAD))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Pineapple T-Shirt"));

        org.assertj.core.api.Assertions.assertThat(productRepository.count()).isEqualTo(1);
    }

    @Test
    void createProduct_withoutToken_isForbidden() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PRODUCT_PAYLOAD))
                .andExpect(status().isForbidden());
    }
}
