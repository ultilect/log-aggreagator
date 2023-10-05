package com.debit.logaggregator.controller;

import com.debit.logaggregator.client.UserUrlClient;
import com.debit.logaggregator.dto.UserUrlDTO;
import com.debit.logaggregator.entity.User;
import com.debit.logaggregator.entity.UserUrl;
import com.debit.logaggregator.repository.UserRepository;
import com.debit.logaggregator.repository.UserUrlRepository;
import com.debit.logaggregator.security.JwtCore;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

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

    @MockBean
    private UserUrlClient userUrlClient;
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
    void createUserUrlWhenOk() throws UnknownHostException {
        final UserUrl testUserUrl = buildTestUserUrl();
        final UserUrlDTO createdUserUrl = new UserUrlDTO(testUserUrl);
        ResponseEntity<String> r = ResponseEntity.status(HttpStatus.OK).body("ok");
        doReturn(r).when(userUrlClient).checkUrl(any());
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
        assertEquals(createdUserUrl.url(), returnedUserUrlDTO.url());
        assertEquals(createdUserUrl.comment(), returnedUserUrlDTO.comment());
        assertEquals(createdUserUrl.nextRequestTime(), returnedUserUrlDTO.nextRequestTime());
        assertEquals(createdUserUrl.periodInMinutes(), returnedUserUrlDTO.periodInMinutes());
        assertEquals(1, userUrlAmount);
    }

    @Test
    void createUserUrlWhenBadUrl() throws UnknownHostException {
        final UserUrl testUserUrl = buildBadUserUrl();
        final UserUrlDTO createdUserUrl = new UserUrlDTO(testUserUrl);
        // Must fail on creating uri
        ResponseEntity<String> r = ResponseEntity.status(HttpStatus.OK).body("ok");
        doReturn(r).when(userUrlClient).checkUrl(any());
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
    void createUserUrlWhenUrlUnavailable() throws UnknownHostException {
        final UserUrl testUserUrl = buildTestUserUrl();
        final UserUrlDTO createdUserUrl = new UserUrlDTO(testUserUrl);
        ResponseEntity<String> r = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        doReturn(r).when(userUrlClient).checkUrl(any());
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
    void updateUserUrlWhenOk() throws UnknownHostException {
        final UserUrl testUserUrl = buildTestUserUrl();
        final User user = this.userRepository.findUserByUsername("createdUser").orElseThrow();
        testUserUrl.setUser(user);
        this.userUrlRepository.save(testUserUrl);
        testUserUrl.setUrl("https://google.com");
        ResponseEntity<String> r = ResponseEntity.status(HttpStatus.OK).build();
        doReturn(r).when(userUrlClient).checkUrl(any());
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
    void updateUserUrlWhenBadNewUrl() throws UnknownHostException{
        final UserUrl testUserUrl = buildTestUserUrl();
        final User user = this.userRepository.findUserByUsername("createdUser").orElseThrow();
        testUserUrl.setUser(user);
        this.userUrlRepository.save(testUserUrl);
        testUserUrl.setUrl("Bad url");
        ResponseEntity<String> r = ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        doReturn(r).when(userUrlClient).checkUrl(any());
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
    void updateUserUrlWhenBadUrlId() throws UnknownHostException {
        final UserUrl testUserUrl = buildTestUserUrl();
        final User user = this.userRepository.findUserByUsername("createdUser").orElseThrow();
        testUserUrl.setUser(user);
        this.userUrlRepository.save(testUserUrl);
        final UserUrlDTO updatedUserUrlDTO = new UserUrlDTO(testUserUrl);
        final UUID randomUUID = UUID.randomUUID();
        ResponseEntity<String> r = ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        doReturn(r).when(userUrlClient).checkUrl(any());
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
