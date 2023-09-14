package com.debit.logaggregator.service;

import com.debit.logaggregator.dto.SignInDTO;
import com.debit.logaggregator.dto.SignUpDTO;

import java.util.Optional;

/**
 * Authentication interface.
 */
public interface AuthService {
    boolean signUp(SignUpDTO signUpDTO);
    Optional<String> signIn(SignInDTO signInDTO);
}
