package com.debit.logaggregator.controller;

import com.debit.logaggregator.dto.UserUrlDTO;
import com.debit.logaggregator.entity.User;
import com.debit.logaggregator.entity.UserUrl;
import com.debit.logaggregator.repository.UserRepository;
import com.debit.logaggregator.repository.UserUrlRepository;
import com.debit.logaggregator.security.JwtCore;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Supposed the user has already logged in
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserUrlControllerTest {
    @LocalServerPort
    private Integer port;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserUrlRepository userUrlRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtCore jwtCore;

    private WebTestClient client;
    private String authUserJwt;

    @BeforeAll
    void addUserAndSetUserJwt() {
        final String username = "createdUser";
        final String password = "12345";
        String testUserPasswordHash = passwordEncoder.encode(password);
        User user = new User();
        user.setUsername(username);
        user.setPassword(testUserPasswordHash);
        user.setEmail("test@test.com");
        user.setPhone("+77777777777");
        userRepository.save(user);
        final Authentication auth =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        authUserJwt = jwtCore.generate(auth);
    }

    @BeforeAll
    void bindWebTestClient() {
        client = WebTestClient
                .bindToServer()
                .baseUrl(String.format("http://localhost:%d", this.port))
                .build();
    }

    @BeforeEach
    void clearUserUrlRepository() {
       userUrlRepository.deleteAll();
    }

    @AfterAll
    void clearUserRepository() {
        userRepository.deleteAll();
    }

    //================================================================================
    // /user/url endpoint tests
    //================================================================================
    @Test
    void getAllUserUrlWithElements() {
        final UserUrl testUserUrl = buildTestUserUrl();
        final User user = this.userRepository.findUserByUsername("createdUser").orElseThrow();
        testUserUrl.setUser(user);
        userUrlRepository.save(testUserUrl);
        final List<UserUrlDTO> expectedBody = new ArrayList<>(List.of(new UserUrlDTO(testUserUrl)));
        final List<UserUrlDTO> returnedUrlList = client
                .get()
                .uri("/user/url")
                .header("Authorization", String.format("Bearer %s", authUserJwt))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(UserUrlDTO.class)
                .returnResult()
                .getResponseBody();
        assertEquals(expectedBody, returnedUrlList);
    }


    @Test
    void getAllUserUrlEmpty() {
        final List<UserUrlDTO> expectedBody = new ArrayList<>();
        final List<UserUrlDTO> returnedUrlList = client
                .get()
                .uri("/user/url")
                .header("Authorization", String.format("Bearer %s", authUserJwt))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(UserUrlDTO.class)
                .returnResult()
                .getResponseBody();
        assertEquals(expectedBody, returnedUrlList);
    }

    @Test
    void getUserUrlByIdWhenOk() {
        final UserUrl testUserUrl = buildTestUserUrl();
        final User user = this.userRepository.findUserByUsername("createdUser").orElseThrow();
        testUserUrl.setUser(user);
        userUrlRepository.save(testUserUrl);
        final UserUrlDTO expectedUserUrl = new UserUrlDTO(testUserUrl);
        final UserUrlDTO returnUserUrl = client
                .get()
                .uri(String.format("/user/url/%s", testUserUrl.getId().toString()))
                .header("Authorization", String.format("Bearer %s", authUserJwt))
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserUrlDTO.class)
                .returnResult()
                .getResponseBody();
        assertEquals(expectedUserUrl, returnUserUrl);
    }

    @Test
    void getUserUrlByIdWhenNotFound() {
        final UUID randomUUID = UUID.randomUUID();
        client
                .get()
                .uri(String.format("/user/url/%s", randomUUID))
                .header("Authorization", String.format("Bearer %s", authUserJwt))
                .exchange()
                .expectStatus().isNotFound();
    }

    //TODO: Return userurl in service
    @Test
    void createUserUrlWhenOk() {
        final UserUrl testUserUrl = buildTestUserUrl();
        final UserUrlDTO createdUserUrl = new UserUrlDTO(testUserUrl);
        final UserUrlDTO returnedUserUrlDTO = client
                .post()
                .uri("/user/url")
                .header("Authorization", String.format("Bearer %s", authUserJwt))
                .bodyValue(createdUserUrl)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserUrlDTO.class)
                .returnResult()
                .getResponseBody();
        final Long userUrlAmount = this.userUrlRepository.count();
        assertEquals(createdUserUrl, returnedUserUrlDTO);
        assertEquals(1, userUrlAmount);
    }

    //TODO: check url aviability
    @Test
    void createUserUrlWhenBadUrl() {
        final UserUrl testUserUrl = buildBadUserUrl();
        final UserUrlDTO createdUserUrl = new UserUrlDTO(testUserUrl);
        client
                .post()
                .uri("/user/url")
                .header("Authorization", String.format("Bearer %s", authUserJwt))
                .bodyValue(createdUserUrl)
                .exchange()
                .expectStatus().isBadRequest();
        final Long userUrlAmount = this.userUrlRepository.count();
        assertEquals(0, userUrlAmount);
    }

    @Test
    void updateUserUrlWhenOk() {
        final UserUrl testUserUrl = buildTestUserUrl();
        final User user = this.userRepository.findUserByUsername("createdUser").orElseThrow();
        testUserUrl.setUser(user);
        this.userUrlRepository.save(testUserUrl);
        testUserUrl.setUrl("https://google.com");
        final UserUrlDTO updatedUserUrlDTO = new UserUrlDTO(testUserUrl);
        final UserUrlDTO returnedUserUrlDTO = client
                .put()
                .uri(String.format("/user/url/%s", testUserUrl.getId()))
                .header("Authorization", String.format("Bearer %s", authUserJwt))
                .bodyValue(updatedUserUrlDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserUrlDTO.class)
                .returnResult()
                .getResponseBody();
        assertEquals(updatedUserUrlDTO, returnedUserUrlDTO);
    }


    @Test
    void updateUserUrlWhenBadNewUrl() {
        final UserUrl testUserUrl = buildTestUserUrl();
        final User user = this.userRepository.findUserByUsername("createdUser").orElseThrow();
        testUserUrl.setUser(user);
        this.userUrlRepository.save(testUserUrl);
        testUserUrl.setUrl("Bad url");
        final UserUrlDTO updatedUserUrlDTO = new UserUrlDTO(testUserUrl);
        client
                .put()
                .uri(String.format("/user/url/%s", testUserUrl.getId()))
                .header("Authorization", String.format("Bearer %s", authUserJwt))
                .bodyValue(updatedUserUrlDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }


    @Test
    void updateUserUrlWhenBadUrlId() {
        final UserUrl testUserUrl = buildTestUserUrl();
        final User user = this.userRepository.findUserByUsername("createdUser").orElseThrow();
        testUserUrl.setUser(user);
        this.userUrlRepository.save(testUserUrl);
        final UserUrlDTO updatedUserUrlDTO = new UserUrlDTO(testUserUrl);
        final UUID randomUUID = UUID.randomUUID();
        client
                .put()
                .uri(String.format("/user/url/%s", randomUUID))
                .header("Authorization", String.format("Bearer %s", authUserJwt))
                .bodyValue(updatedUserUrlDTO)
                .exchange()
                .expectStatus().isNotFound();
    }


    @Test
    void deleteUserUrl() {
        final UserUrl testUserUrl = buildTestUserUrl();
        final User user = this.userRepository.findUserByUsername("createdUser").orElseThrow();
        testUserUrl.setUser(user);
        this.userUrlRepository.save(testUserUrl);
        client
                .delete()
                .uri(String.format("/user/url/%s", testUserUrl.getId()))
                .header("Authorization", String.format("Bearer %s", authUserJwt))
                .exchange()
                .expectStatus().isOk();
        final Long userUrlAmount = this.userUrlRepository.count();
        assertEquals(0, userUrlAmount);
    }

    @Test
    void deleteUserUrlWhenNoSuchId() {
        final UserUrl testUserUrl = buildTestUserUrl();
        final User user = this.userRepository.findUserByUsername("createdUser").orElseThrow();
        testUserUrl.setUser(user);
        this.userUrlRepository.save(testUserUrl);
        final UUID randomUUID = UUID.randomUUID();
        client
                .delete()
                .uri(String.format("/user/url/%s", randomUUID))
                .header("Authorization", String.format("Bearer %s", authUserJwt))
                .exchange()
                .expectStatus().isOk();
        final Long userUrlAmount = this.userUrlRepository.count();
        assertEquals(1, userUrlAmount);
    }

    private static UserUrl buildTestUserUrl() {
        UserUrl userUrl = new UserUrl();
        userUrl.setUrl("http://example.com");
        userUrl.setComment("UserUrl test example");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        userUrl.setNextRequestTime(calendar.getTime());
        userUrl.setPeriodInMinutes(10);
        return userUrl;
    }

    private static UserUrl buildBadUserUrl() {
        UserUrl userUrl = new UserUrl();
        userUrl.setUrl("Bad url");
        userUrl.setComment("UserUrl test example");

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        userUrl.setNextRequestTime(calendar.getTime());
        userUrl.setPeriodInMinutes(10);
        return userUrl;
    }
}
