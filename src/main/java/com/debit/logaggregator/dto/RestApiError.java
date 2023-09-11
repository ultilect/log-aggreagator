package com.debit.logaggregator.dto;

public record RestApiError(Integer status, String error, String path) {
    public RestApiError() {
        this(0, "", "");
    }
}
