package com.debit.logaggregator.controller;

import com.debit.logaggregator.dto.RestApiError;
import com.debit.logaggregator.dto.SignInDTO;
import com.debit.logaggregator.dto.SignUpDTO;
import com.debit.logaggregator.entity.User;
import com.debit.logaggregator.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {
    @LocalServerPort
    private Integer port;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private final TestRestTemplate testRestTemplate = new TestRestTemplate();

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    //================================================================================
    // /auth/signup endpoint tests
    //================================================================================
    @Test
    void signUpNewUserOk() {
        final User user = createTestUser();
        HttpEntity<SignUpDTO> request = new HttpEntity<>(new SignUpDTO(user.getUsername(), user.getEmail(), user.getPhone(), user.getPassword()));
        ResponseEntity<String> responce =
               this.testRestTemplate.postForEntity(
                        String.format("http://localhost:%d/auth/signup",this.port),
                       request,
                       String.class
                );
        final Long userAmount = this.userRepository.count();
        assertEquals(HttpStatus.CREATED, responce.getStatusCode());
        assertEquals("ok", responce.getBody());
        assertEquals(1, userAmount);
    }

    @Test
    void signUpNewUserWhenUserWithSameUsernameExist() {
        final User user = createTestUser();
        final User sameUser = createAnotherTestUser();
        sameUser.setUsername(user.getUsername());
        userRepository.save(user);

        HttpEntity<SignUpDTO> request = new HttpEntity<>(new SignUpDTO(sameUser.getUsername(), sameUser.getEmail(), sameUser.getPhone(), sameUser.getPassword()));
        ResponseEntity<RestApiError> responce =
                this.testRestTemplate.postForEntity(
                        String.format("http://localhost:%d/auth/signup",this.port),
                        request,
                        RestApiError.class
                );
        if (responce.getBody() == null) {
            fail();
        }
        final Long userAmount = this.userRepository.count();
        assertEquals(HttpStatus.BAD_REQUEST, responce.getStatusCode());
        assertEquals("/auth/signup", responce.getBody().path());
        assertEquals(400, responce.getBody().status());
        assertEquals("Error while signUp", responce.getBody().error());
        assertEquals(1, userAmount);
    }

    @Test
    void signUpNewUserWhenUserWithSameEmailExist() {
        final User user = createTestUser();
        final User sameUser = createAnotherTestUser();
        sameUser.setEmail(user.getEmail());
        userRepository.save(user);

        HttpEntity<SignUpDTO> request = new HttpEntity<>(new SignUpDTO(sameUser.getUsername(), sameUser.getEmail(), sameUser.getPhone(), sameUser.getPassword()));
        ResponseEntity<RestApiError> responce =
                this.testRestTemplate.postForEntity(
                        String.format("http://localhost:%d/auth/signup",this.port),
                        request,
                        RestApiError.class
                );
        if (responce.getBody() == null) {
            fail();
        }
        final Long userAmount = this.userRepository.count();
        assertEquals(HttpStatus.BAD_REQUEST, responce.getStatusCode());
        assertEquals("/auth/signup", responce.getBody().path());
        assertEquals(400, responce.getBody().status());
        assertEquals("Error while signUp", responce.getBody().error());
        assertEquals(1, userAmount);
    }

    @Test
    void signUpNewUserWhenUserWithSamePhoneExist() {
        final User user = createTestUser();
        final User sameUser = createAnotherTestUser();
        sameUser.setPhone(user.getPhone());
        userRepository.save(user);

        HttpEntity<SignUpDTO> request = new HttpEntity<>(new SignUpDTO(sameUser.getUsername(), sameUser.getEmail(), sameUser.getPhone(), sameUser.getPassword()));
        ResponseEntity<RestApiError> responce =
                this.testRestTemplate.postForEntity(
                        String.format("http://localhost:%d/auth/signup",this.port),
                        request,
                        RestApiError.class
                );
        if (responce.getBody() == null) {
            fail();
        }
        final Long userAmount = this.userRepository.count();
        assertEquals(HttpStatus.BAD_REQUEST, responce.getStatusCode());
        assertEquals("/auth/signup", responce.getBody().path());
        assertEquals(400, responce.getBody().status());
        assertEquals("Error while signUp", responce.getBody().error());
        assertEquals(1, userAmount);
    }

    @Test
    void signUpWhenPasswordIsNull() {
        final User user = createTestUser();
        user.setPassword(null);
        HttpEntity<SignUpDTO> request = new HttpEntity<>(new SignUpDTO(user.getUsername(), user.getEmail(), user.getPhone(), user.getPassword()));
        ResponseEntity<RestApiError> responce =
                this.testRestTemplate.postForEntity(
                        String.format("http://localhost:%d/auth/signup",this.port),
                        request,
                        RestApiError.class
                );
        if (responce.getBody() == null) {
            fail();
        }
        final Long userAmount = this.userRepository.count();
        assertEquals(HttpStatus.BAD_REQUEST, responce.getStatusCode());
        assertEquals("/auth/signup", responce.getBody().path());
        assertEquals(400, responce.getBody().status());
        assertEquals("Error while signUp", responce.getBody().error());;
        assertEquals(0, userAmount);
    }
    @Test
    void signUpWhenUsernameIsNull() {
        final User user = createTestUser();
        user.setUsername(null);
        HttpEntity<SignUpDTO> request = new HttpEntity<>(new SignUpDTO(user.getUsername(), user.getEmail(), user.getPhone(), user.getPassword()));
        ResponseEntity<RestApiError> responce =
                this.testRestTemplate.postForEntity(
                        String.format("http://localhost:%d/auth/signup",this.port),
                        request,
                        RestApiError.class
                );
        if (responce.getBody() == null) {
            fail();
        }
        final Long userAmount = this.userRepository.count();
        assertEquals(HttpStatus.BAD_REQUEST, responce.getStatusCode());
        assertEquals("/auth/signup", responce.getBody().path());
        assertEquals(400, responce.getBody().status());
        assertEquals("Error while signUp", responce.getBody().error());;
        assertEquals(0, userAmount);
    }
    @Test
    void signUpWhenEmailIsNull() {
        final User user = createTestUser();
        user.setEmail(null);
        HttpEntity<SignUpDTO> request = new HttpEntity<>(new SignUpDTO(user.getUsername(), user.getEmail(), user.getPhone(), user.getPassword()));
        ResponseEntity<RestApiError> responce =
                this.testRestTemplate.postForEntity(
                        String.format("http://localhost:%d/auth/signup",this.port),
                        request,
                        RestApiError.class
                );
        if (responce.getBody() == null) {
            fail();
        }
        final Long userAmount = this.userRepository.count();
        assertEquals(HttpStatus.BAD_REQUEST, responce.getStatusCode());
        assertEquals("/auth/signup", responce.getBody().path());
        assertEquals(400, responce.getBody().status());
        assertEquals("Error while signUp", responce.getBody().error());;
        assertEquals(0, userAmount);
    }
    @Test
    void signUpWhenPhoneIsNull() {
        final User user = createTestUser();
        user.setPhone(null);
        HttpEntity<SignUpDTO> request = new HttpEntity<>(new SignUpDTO(user.getUsername(), user.getEmail(), user.getPhone(), user.getPassword()));
        ResponseEntity<String> responce =
                this.testRestTemplate.postForEntity(
                        String.format("http://localhost:%d/auth/signup",this.port),
                        request,
                        String.class
                );
        if (responce.getBody() == null) {
            fail();
        }
        final Long userAmount = this.userRepository.count();
        assertEquals(HttpStatus.CREATED, responce.getStatusCode());
        assertEquals("ok", responce.getBody());
        assertEquals(1, userAmount);
    }


    //================================================================================
    // /auth/signin endpoint tests
    //================================================================================

    @Test
    void signInOk() {
        final User user = createTestUser();
        final String userPassword = "12345678";
        user.setPassword(passwordEncoder.encode(userPassword));
        this.userRepository.save(user);
        HttpEntity<SignInDTO> request = new HttpEntity<>(new SignInDTO(user.getUsername(), userPassword));
        ResponseEntity<String> responce =
                this.testRestTemplate.postForEntity(
                        String.format("http://localhost:%d/auth/signin",this.port),
                        request,
                        String.class
                );
        if (responce.getBody() == null) {
            fail();
        }
        assertEquals(HttpStatus.OK, responce.getStatusCode());
        assertEquals("ok", responce.getBody());
        assertNotNull(responce.getHeaders().get(HttpHeaders.AUTHORIZATION));
    }

    @Test
    void signInWhenBadPassword() {
        final User user = createTestUser();
        final String userPassword = "12345678";
        user.setPassword(passwordEncoder.encode(userPassword));
        this.userRepository.save(user);
        HttpEntity<SignInDTO> request = new HttpEntity<>(new SignInDTO(user.getUsername(), "1"));
        ResponseEntity<String> responce =
                this.testRestTemplate.postForEntity(
                        String.format("http://localhost:%d/auth/signin",this.port),
                        request,
                        String.class
                );
        assertEquals(HttpStatus.UNAUTHORIZED, responce.getStatusCode());
    }

    @Test
    void signInWhenNoUser() {
        final User user = createTestUser();
        final String userPassword = "12345678";
        user.setPassword(passwordEncoder.encode(userPassword));
        this.userRepository.save(user);
        HttpEntity<SignInDTO> request = new HttpEntity<>(new SignInDTO("1", "1"));
        ResponseEntity<String> responce =
                this.testRestTemplate.postForEntity(
                        String.format("http://localhost:%d/auth/signin",this.port),
                        request,
                        String.class
                );
        assertEquals(HttpStatus.UNAUTHORIZED, responce.getStatusCode());
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
