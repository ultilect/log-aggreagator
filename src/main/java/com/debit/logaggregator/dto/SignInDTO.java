package com.debit.logaggregator.dto;

/**
 * Body for auth.
 * @param username - username(unique)
 * @param password - password
 */
public record SignInDTO(String username, String password) {
}
