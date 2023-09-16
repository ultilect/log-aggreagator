package com.debit.logaggregator.service.impl;

import com.debit.logaggregator.dto.UserDTO;
import com.debit.logaggregator.repository.UserRepository;
import com.debit.logaggregator.entity.User;
import com.debit.logaggregator.security.UserDetailsImpl;
import graphql.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Bogdan Lesin
 */
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers() {
        return Lists.newArrayList(this.userRepository.findAll());
    }

    public UserDTO findUserByUsername(final String username) throws UsernameNotFoundException {
        return new UserDTO(userRepository.findUserByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User %s not found", username))));
    }
    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final User user = userRepository.findUserByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User %s not found", username)));
        return UserDetailsImpl.build(user);
    }
}
