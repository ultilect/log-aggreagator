package com.debit.logaggregator.service;

import com.debit.logaggregator.dto.SignInDTO;
import com.debit.logaggregator.dto.SignUpDTO;
import com.debit.logaggregator.entity.User;

import java.util.Optional;

public interface AuthService {
    boolean signUp(SignUpDTO signUpDTO);
    Optional<User> signIn(SignInDTO signInDTO);
}
