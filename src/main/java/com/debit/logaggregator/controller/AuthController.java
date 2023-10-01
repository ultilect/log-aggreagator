package com.debit.logaggregator.controller;

import com.debit.logaggregator.dto.RestApiError;
import com.debit.logaggregator.dto.SignInDTO;
import com.debit.logaggregator.dto.SignUpDTO;
import com.debit.logaggregator.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Authentication controller.
 */
@RestController
@RequestMapping("auth")
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @Autowired
    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("signup")
    @SuppressWarnings("checkstyle:ReturnCount")
    ResponseEntity<?> signUp(@RequestBody final SignUpDTO signUp) {
        LOGGER.trace("New user signUp with username {}", signUp.username());
        try {
            final boolean status = this.authService.signUp(signUp);
            if (!status) {
                final RestApiError badRequestError = new RestApiError(400, "Error while signUp", "/auth/signup");
                LOGGER.trace("Bad user signUp data username {}", signUp.username());
                return ResponseEntity.status(400).body(badRequestError);
            }
            LOGGER.trace("Success username {}", signUp.username());
            return ResponseEntity.status(201).body("ok");
        } catch (Exception ex) {
            final RestApiError unknownError = new RestApiError(500, ex.getMessage(), "auth/signup");
            LOGGER.error("Unknown error signIn", ex);
            return ResponseEntity.status(500).body(unknownError);
        }
    }

   @PostMapping(value = "signin", produces = APPLICATION_JSON_VALUE)
   ResponseEntity<?> signIn(@RequestBody final SignInDTO signIn) {
        LOGGER.trace("SignIn username {}", signIn.username());
        try {
            return this.authService.signIn(signIn)
                    .map((jwt) -> {
                        LOGGER.trace("Success signIn username {}", signIn.username());
                        return ResponseEntity.status(HttpStatus.OK)
                                .header(HttpHeaders.AUTHORIZATION, jwt)
                                .body("ok");
                    })
                    .orElseGet(() -> {
                        LOGGER.trace("Bad credentials signIn username {}", signIn.username());
                        return ResponseEntity.status(401).build();
                    });
        } catch (Exception ex) {
            final RestApiError unknownError = new RestApiError(500, ex.getMessage(), "auth/signin");
            LOGGER.error("Unknown error signIn", ex);
            return ResponseEntity.status(500).body(unknownError);
        }
   }
}
