package com.debit.logaggregator.controller;

import com.debit.logaggregator.dto.RestApiError;
import com.debit.logaggregator.dto.SignInDTO;
import com.debit.logaggregator.dto.SignUpDTO;
import com.debit.logaggregator.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Authentication controller.
 */
@RestController
@RequestMapping("auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(final AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("signup")
    @SuppressWarnings("checkstyle:ReturnCount")
    ResponseEntity<?> signUp(@RequestBody final SignUpDTO signUp) {
        try {
            final boolean status = this.authService.signUp(signUp);
            if (!status) {
                final RestApiError badRequestError = new RestApiError(400, "Error while signUp", "/auth/signup");
                return ResponseEntity.status(400).body(badRequestError);
            }
            return ResponseEntity.status(201).body("ok");
        } catch (Exception ex) {
            final RestApiError unknownError = new RestApiError(500, ex.getMessage(), "auth/signup");
            return ResponseEntity.status(500).body(unknownError);
        }
    }

   @PostMapping(value = "signin", produces = APPLICATION_JSON_VALUE)
   ResponseEntity<?> signIn(@RequestBody final SignInDTO signIn) {
        try {
            return this.authService.signIn(signIn)
                    .map((jwt) -> ResponseEntity.status(HttpStatus.OK)
                            .header(HttpHeaders.AUTHORIZATION, jwt)
                            .body("ok"))
                    .orElseGet(() -> ResponseEntity.status(401).build());
        } catch (Exception ex) {
            final RestApiError unknownError = new RestApiError(500, ex.getMessage(), "auth/signin");
            return ResponseEntity.status(500).body(unknownError);
        }
   }
}
