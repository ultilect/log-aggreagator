package com.debit.logaggregator.controller;

import com.debit.logaggregator.dto.RestApiError;
import com.debit.logaggregator.dto.UserUrlDTO;
import com.debit.logaggregator.security.UserDetailsImpl;
import com.debit.logaggregator.service.UserUrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Bogdan Lesin
 */
@RestController
@RequestMapping("user/url")
@SuppressWarnings("ReturnCount")
public class UserUrlController {
    // Response common params
    private static final String USER_NOT_FOUND_BODY = "User not found";
    private static final String BASIC_URL = "/user/url";
    private static final Logger LOGGER = LoggerFactory.getLogger(UserUrlController.class);
    private final UserUrlService userUrlService;


    @Autowired
    public UserUrlController(final UserUrlService userUrlService) {
        this.userUrlService = userUrlService;
    }

    @GetMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<?> getUserUrl(final @PathVariable("id") UUID id) {
        final UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        if (user == null) {
            final RestApiError noUserError =
                    new RestApiError(HttpStatus.NOT_FOUND.value(), USER_NOT_FOUND_BODY, BASIC_URL);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noUserError);
        }
        LOGGER.trace("getUserUrl for username {}", user.getUsername());
        return this.userUrlService
                .getEntity(id, user.getUserId()).map((userUrl) -> {
                    LOGGER.trace("getUserUrl successful for username {}", user.getUsername());
                    return ResponseEntity.status(HttpStatus.OK).body(userUrl);
                })
                .orElseGet(() -> {
                    LOGGER.trace("getUserUrl url not found for username {}", user.getUsername());
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                });
    }
    @GetMapping
    ResponseEntity<?> getAllUserUrl() {
        final UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        if (user == null) {
            final RestApiError noUserError =
                    new RestApiError(HttpStatus.NOT_FOUND.value(), USER_NOT_FOUND_BODY, BASIC_URL);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noUserError);
        }
        LOGGER.trace("getAllUserUrl for username {}", user.getUsername());
        try {
            return ResponseEntity.ok(this.userUrlService.getAll(user.getUserId()));
        } catch (Exception ex) {
            LOGGER.error("getAllUserUrl unknown error for username {}", user.getUsername());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    ResponseEntity<?> createUserUrl(@RequestBody final UserUrlDTO userUrlDTO) {
        final UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        if (user == null) {
            final RestApiError noUserError =
                    new RestApiError(HttpStatus.NOT_FOUND.value(), USER_NOT_FOUND_BODY, BASIC_URL);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noUserError);
        }
        LOGGER.trace("createUserUrl for username {}", user.getUsername());
        try {
            this.userUrlService.saveEntity(userUrlDTO, user.getUserId());
            LOGGER.trace("createUserUrl successful for username {}", user.getUsername());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (NoSuchElementException ex) {
            final RestApiError error = new RestApiError(HttpStatus.NOT_FOUND.value(), ex.getMessage(), BASIC_URL);
            LOGGER.warn("createUserUrl warning: {} for username {}", ex.getMessage(), user.getUsername());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PutMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<?> updateUserUrl(@PathVariable("id") final UUID id, @RequestBody final UserUrlDTO userUrlDTO) {
            final UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getPrincipal();
            if (user == null) {
                final RestApiError noUserError =
                        new RestApiError(HttpStatus.NOT_FOUND.value(), USER_NOT_FOUND_BODY, BASIC_URL);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noUserError);
            }
            LOGGER.trace("updateUserUrl for username {}", user.getUsername());
            return this.userUrlService.updateEntity(id, userUrlDTO, user.getUserId())
                    .map((userUrl) -> {
                        LOGGER.trace("updateUserUrl succesful for username {}", user.getUsername());
                        return ResponseEntity.status(HttpStatus.OK).body(userUrl);
                    })
                    .orElseGet(() -> {
                        LOGGER
                                .trace("updateUserUrl unable to update {} with id {} for username {}",
                                        userUrlDTO,
                                        id,
                                        user.getUsername());
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                    });
    }
    @DeleteMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<?> deleteUserUrl(@PathVariable("id") final UUID id) {
        final UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        if (user == null) {
            final RestApiError noUserError =
                    new RestApiError(HttpStatus.NOT_FOUND.value(), USER_NOT_FOUND_BODY, BASIC_URL);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(noUserError);
        }
        this.userUrlService.deleteEntity(id, user.getUserId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
