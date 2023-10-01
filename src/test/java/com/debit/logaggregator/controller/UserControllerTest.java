package com.debit.logaggregator.controller;

import com.debit.logaggregator.dto.UserDTO;
import com.debit.logaggregator.entity.User;
import com.debit.logaggregator.repository.UserRepository;
import com.debit.logaggregator.security.JwtCore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {
    @LocalServerPort
    private Integer port;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtCore jwtCore;

    private WebTestClient client;

    @BeforeAll
    void addUser() {
        String testUserPasswordHash = passwordEncoder.encode("12345");
        User user = new User();
        user.setUsername("createdUser");
        user.setPassword(testUserPasswordHash);
        user.setEmail("test@test.com");
        user.setPhone("+77777777777");
        userRepository.save(user);
    }

    @BeforeAll
    void bindWebTestClient() {
        client = WebTestClient
                .bindToServer()
                .baseUrl(String.format("http://localhost:%d", this.port))
                .build();
    }
    //================================================================================
    // /user endpoint tests
    //================================================================================
    @Test
    void getUserOk() {
        final Authentication auth = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken("createdUser", "12345"));
        final String jwt = jwtCore.generate(auth);
        final User user = this.userRepository.findUserByUsername("createdUser").orElseThrow();
        final UserDTO expectedUser = new UserDTO(user);
        final UserDTO returnedUser = client
                .get()
                .uri("/user")
                .header("Authorization", String.format("Bearer %s", jwt))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(UserDTO.class)
                .returnResult()
                .getResponseBody();
        assertEquals(expectedUser, returnedUser);
    }

    @Test
    void getUserWithoutAuthorization() {
        client
                .get()
                .uri("/user")
                .exchange()
                .expectStatus().is4xxClientError();
    }
}
