package com.debit.logaggregator.dto;

/**
 * TODO: VALIDATION
 * Body for registration.
 * @param username - login, which should be unique
 * @param email - email, which should be unique
 * @param phone - phone, which should be unique
 * @param password - password
 */
public record SignUpDTO(String username, String email, String phone, String password) {
}
