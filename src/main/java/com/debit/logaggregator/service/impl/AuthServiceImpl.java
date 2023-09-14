package com.debit.logaggregator.service.impl;

import com.debit.logaggregator.dto.SignInDTO;
import com.debit.logaggregator.dto.SignUpDTO;
import com.debit.logaggregator.entity.User;
import com.debit.logaggregator.repository.UserRepository;
import com.debit.logaggregator.security.JwtCore;
import com.debit.logaggregator.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Bogdan Lesin
 */
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtCore jwtCore;
    @Autowired
    public AuthServiceImpl(final UserRepository userRepository,
                           final PasswordEncoder passwordEncoder,
                           final AuthenticationManager authenticationManager,
                           final JwtCore jwtCore) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtCore = jwtCore;
    }

    @Override
    public boolean signUp(final SignUpDTO signUpDTO) {
        try {
            final String encodedPassword = passwordEncoder.encode(signUpDTO.password());
            //TODO: builder
            final User user = new User();
            user.setEmail(signUpDTO.email());
            user.setPassword(encodedPassword);
            user.setPhone(signUpDTO.phone());
            user.setUsername(signUpDTO.username());
            this.userRepository.save(user);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public Optional<String> signIn(final SignInDTO signInDTO) {
        try {
            final Authentication auth = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(signInDTO.username(),
                                    signInDTO.password())
                    );
            SecurityContextHolder.getContext().setAuthentication(auth);
            final String jwt = jwtCore.generate(auth);
            return Optional.of(jwt);
        } catch (Exception ex) {
            return Optional.<String>empty();
        }
    }
}
