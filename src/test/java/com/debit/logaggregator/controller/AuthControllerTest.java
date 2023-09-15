package com.debit.logaggregator.controller;

import com.debit.logaggregator.dto.RestApiError;
import com.debit.logaggregator.entity.User;
import com.debit.logaggregator.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {
    @LocalServerPort
    private Integer port;

    @Autowired
    private UserRepository userRepository;
    private final TestRestTemplate testRestTemplate = new TestRestTemplate();

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void signUpNewUserOk() {
        final User user = createTestUser();
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity<String> responce =
               this.testRestTemplate.postForEntity(
                        String.format("http://localhost:%d/auth/signup",this.port),
                       request,
                       String.class
                );
        assertEquals(HttpStatus.CREATED, responce.getStatusCode());
        assertEquals("ok", responce.getBody());
    }

    @Test
    void signUpNewUserWhenUserWithSameUsernameExist() {
        final User user = createTestUser();
        final User sameUser = createAnotherTestUser();
        sameUser.setUsername(user.getUsername());
        userRepository.save(user);

        HttpEntity<User> request = new HttpEntity<>(sameUser);
        ResponseEntity<RestApiError> responce =
                this.testRestTemplate.postForEntity(
                        String.format("http://localhost:%d/auth/signup",this.port),
                        request,
                        RestApiError.class
                );
        if (responce.getBody() == null) {
            fail();
        }
        assertEquals(HttpStatus.BAD_REQUEST, responce.getStatusCode());
        assertEquals("/auth/signup", responce.getBody().path());
        assertEquals(400, responce.getBody().status());
        assertEquals("Error while signUp", responce.getBody().error());
    }

    @Test
    void signUpNewUserWhenUserWithSameEmailExist() {
        final User user = createTestUser();
        final User sameUser = createAnotherTestUser();
        sameUser.setEmail(user.getEmail());
        userRepository.save(user);

        HttpEntity<User> request = new HttpEntity<>(sameUser);
        ResponseEntity<RestApiError> responce =
                this.testRestTemplate.postForEntity(
                        String.format("http://localhost:%d/auth/signup",this.port),
                        request,
                        RestApiError.class
                );
        if (responce.getBody() == null) {
            fail();
        }
        assertEquals(HttpStatus.BAD_REQUEST, responce.getStatusCode());
        assertEquals("/auth/signup", responce.getBody().path());
        assertEquals(400, responce.getBody().status());
        assertEquals("Error while signUp", responce.getBody().error());
    }

    @Test
    void signUpNewUserWhenUserWithSamePhoneExist() {
        final User user = createTestUser();
        final User sameUser = createAnotherTestUser();
        sameUser.setPhone(user.getPhone());
        userRepository.save(user);

        HttpEntity<User> request = new HttpEntity<>(sameUser);
        ResponseEntity<RestApiError> responce =
                this.testRestTemplate.postForEntity(
                        String.format("http://localhost:%d/auth/signup",this.port),
                        request,
                        RestApiError.class
                );
        if (responce.getBody() == null) {
            fail();
        }
        assertEquals(HttpStatus.BAD_REQUEST, responce.getStatusCode());
        assertEquals("/auth/signup", responce.getBody().path());
        assertEquals(400, responce.getBody().status());
        assertEquals("Error while signUp", responce.getBody().error());
    }

    @Test
    void signUpWhenPasswordIsNull() {
        final User user = createTestUser();
        user.setPassword(null);
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity<RestApiError> responce =
                this.testRestTemplate.postForEntity(
                        String.format("http://localhost:%d/auth/signup",this.port),
                        request,
                        RestApiError.class
                );
        if (responce.getBody() == null) {
            fail();
        }
        assertEquals(HttpStatus.BAD_REQUEST, responce.getStatusCode());
        assertEquals("/auth/signup", responce.getBody().path());
        assertEquals(400, responce.getBody().status());
        assertEquals("Error while signUp", responce.getBody().error());;
    }
    @Test
    void signUpWhenUsernameIsNull() {
        final User user = createTestUser();
        user.setUsername(null);
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity<RestApiError> responce =
                this.testRestTemplate.postForEntity(
                        String.format("http://localhost:%d/auth/signup",this.port),
                        request,
                        RestApiError.class
                );
        if (responce.getBody() == null) {
            fail();
        }
        assertEquals(HttpStatus.BAD_REQUEST, responce.getStatusCode());
        assertEquals("/auth/signup", responce.getBody().path());
        assertEquals(400, responce.getBody().status());
        assertEquals("Error while signUp", responce.getBody().error());;
    }
    @Test
    void signUpWhenEmailIsNull() {
        final User user = createTestUser();
        user.setEmail(null);
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity<RestApiError> responce =
                this.testRestTemplate.postForEntity(
                        String.format("http://localhost:%d/auth/signup",this.port),
                        request,
                        RestApiError.class
                );
        if (responce.getBody() == null) {
            fail();
        }
        assertEquals(HttpStatus.BAD_REQUEST, responce.getStatusCode());
        assertEquals("/auth/signup", responce.getBody().path());
        assertEquals(400, responce.getBody().status());
        assertEquals("Error while signUp", responce.getBody().error());;
    }
    @Test
    void signUpWhenPhoneIsNull() {
        final User user = createTestUser();
        user.setPhone(null);
        HttpEntity<User> request = new HttpEntity<>(user);
        ResponseEntity<String> responce =
                this.testRestTemplate.postForEntity(
                        String.format("http://localhost:%d/auth/signup",this.port),
                        request,
                        String.class
                );
        if (responce.getBody() == null) {
            fail();
        }
        assertEquals(HttpStatus.CREATED, responce.getStatusCode());
        assertEquals("ok", responce.getBody());
    }
    private static User createTestUser() {
        final User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("12345678");
        testUser.setPhone("+79888888888");
        testUser.setEmail("test@email.com");
        return testUser;
    }

    private static User createAnotherTestUser() {
        final User testUser = new User();
        testUser.setUsername("testUser2");
        testUser.setPassword("123456782");
        testUser.setPhone("+798888888882");
        testUser.setEmail("test2@email.com");
        return testUser;
    }
}
