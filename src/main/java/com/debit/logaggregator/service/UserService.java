package com.debit.logaggregator.service;

import com.debit.logaggregator.repository.UserRepository;
import com.debit.logaggregator.entity.User;
import graphql.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Bogdan Lesin
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers() {
        return Lists.newArrayList(this.userRepository.findAll());
    }
}
