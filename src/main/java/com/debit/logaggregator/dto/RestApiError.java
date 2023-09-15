package com.debit.logaggregator.dto;

/**
 * Response for errors in REST.
 * @author Bogdan Lesin
 * @param status - http status code
 * @param error - description of error
 * @param path - relative url
 */
public record RestApiError(Integer status, String error, String path) {
    public RestApiError() {
        this(0, "", "");
    }
}
