package com.debit.logaggregator.controller;

import com.debit.logaggregator.dto.UserDTO;
import com.debit.logaggregator.service.UserService;
import com.debit.logaggregator.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String check() {
        return "check";
    }
    @GetMapping(path = "all", produces = APPLICATION_JSON_VALUE)
    public List<UserDTO> findAllUsers() {
        final List<User> users = this.userService.findAllUsers();
        return users.stream().map(UserDTO::new).collect(Collectors.toList());
    }

}
