package com.debit.logaggregator.service.impl;

import com.debit.logaggregator.dto.SignInDTO;
import com.debit.logaggregator.dto.SignUpDTO;
import com.debit.logaggregator.entity.User;
import com.debit.logaggregator.repository.UserRepository;
import com.debit.logaggregator.security.JwtCore;
import com.debit.logaggregator.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean signUp(SignUpDTO signUpDTO) {
        try {
            final String encodedPassword = passwordEncoder.encode(signUpDTO.password());
            //TODO: builder
            User user = new User();
            user.setEmail(signUpDTO.email());
            user.setPassword(encodedPassword);
            user.setPhone(signUpDTO.phone());
            user.setUsername(signUpDTO.username());
            this.userRepository.save(user);
            return true;
        } catch(Exception ex) {
            return false;
        }
    }

    @Override
    public Optional<User> signIn(SignInDTO signInDTO) {
        return Optional.empty();
    }
}
