package com.debit.logaggregator.controller;

import com.debit.logaggregator.dto.RestApiError;
import com.debit.logaggregator.dto.UserDTO;
import com.debit.logaggregator.entity.User;
import com.debit.logaggregator.security.UserDetailsImpl;
import com.debit.logaggregator.service.impl.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Контроллер для работы с пользователем.
 * @author Bogdan Lesin
 */
@RestController
@RequestMapping("user")
@SuppressWarnings("ReturnCount")
public class UserController {
    // Response common params
    private static final String BASIC_URL = "/user";

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;


    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getUser() {
        LOGGER.trace("getUser request");
        try {
            final UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();
            if (user == null) {
                LOGGER.trace("getUser no auth");
                final RestApiError noUserError = new RestApiError(404, "User not found", BASIC_URL);
                return ResponseEntity.status(404).body(noUserError);
            }
            LOGGER.trace("successful getUser");
            return ResponseEntity.status(HttpStatus.OK).body(this.userService.findUserByUserId(user.getUserId()));
        } catch (Exception ex) {
            final RestApiError unknownError = new RestApiError(500, ex.getMessage(), BASIC_URL);
            return ResponseEntity.status(500).body(unknownError);
        }
    }

    @GetMapping(path = "all", produces = APPLICATION_JSON_VALUE)
    public List<UserDTO> findAllUsers() {
        final List<User> users = this.userService.findAllUsers();
        return users.stream().map(UserDTO::new).collect(Collectors.toList());
    }

}
