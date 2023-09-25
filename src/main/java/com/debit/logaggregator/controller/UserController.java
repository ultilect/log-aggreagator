package com.debit.logaggregator.controller;

import com.debit.logaggregator.dto.RestApiError;
import com.debit.logaggregator.dto.UserDTO;
import com.debit.logaggregator.security.UserDetailsImpl;
import com.debit.logaggregator.service.impl.UserService;
import com.debit.logaggregator.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getUser() {
        logger.trace("getUser request");
        try {
            UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();
            if (user == null) {
                logger.trace("getUser no auth");
                final RestApiError noUserError = new RestApiError(404, "User not found", "/user");
                return ResponseEntity.status(404).body(noUserError);
            }
            logger.trace("successful getUser");
            return ResponseEntity.status(HttpStatus.OK).body(this.userService.findUserByUserId(user.getUserId()));
        } catch (UsernameNotFoundException ex) {
            //Beacause of auth unnecessary check
            logger.warn("Insufficient token: {}", ex.getMessage());
            final RestApiError noUser = new RestApiError(404, "No such user", "/user");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noUser);
        }catch (Exception ex) {
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
