package com.debit.logaggregator.controller;

import com.debit.logaggregator.dto.RestApiError;
import com.debit.logaggregator.dto.SignUpDTO;
import com.debit.logaggregator.security.JwtCore;
import com.debit.logaggregator.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtCore jwtCore;

    @Autowired
    public AuthController(AuthService authService, AuthenticationManager authenticationManager, JwtCore jwtCore) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.jwtCore = jwtCore;
    }

    @PostMapping("signup")
    ResponseEntity<?> signUp(@RequestBody SignUpDTO signUp) {
        try {
            final boolean status = this.authService.signUp(signUp);
            if(!status) {
                final RestApiError badRequestError = new RestApiError(400, "Error while signUp", "/auth/signup");
                return ResponseEntity.status(400).body(badRequestError);
            }
            return ResponseEntity.status(200).body("ok");
        } catch (Exception ex) {
            final RestApiError unknownError = new RestApiError(500, ex.getMessage(), "auth/signup");
            return ResponseEntity.status(500).body(unknownError);
        }
    }
}
