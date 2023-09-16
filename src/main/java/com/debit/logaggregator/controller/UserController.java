package com.debit.logaggregator.controller;

import com.debit.logaggregator.dto.RestApiError;
import com.debit.logaggregator.dto.UserDTO;
import com.debit.logaggregator.service.impl.UserService;
import com.debit.logaggregator.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Контроллер для работы с пользователем.
 * @author Bogdan Lesin
 */
@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getUser(final Principal principal) {
        try {
            if (principal == null) {
                return null;
            }
            return ResponseEntity.status(HttpStatus.OK).body(this.userService.findUserByUsername(principal.getName()));
        } catch (Exception ex) {
            final RestApiError unknownError = new RestApiError(500, ex.getMessage(), "/user");
            return ResponseEntity.status(500).body(unknownError);
        }
    }

    @GetMapping(path = "all", produces = APPLICATION_JSON_VALUE)
    public List<UserDTO> findAllUsers() {
        final List<User> users = this.userService.findAllUsers();
        return users.stream().map(UserDTO::new).collect(Collectors.toList());
    }

}
