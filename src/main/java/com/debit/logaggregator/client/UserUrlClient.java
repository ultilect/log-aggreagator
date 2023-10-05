package com.debit.logaggregator.client;

import feign.Headers;
import feign.RequestLine;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.UnknownHostException;

/**
 * Client is used for requesting user endpoints.
 */
public interface UserUrlClient {

    @RequestLine("HEAD")
    @Headers("Keep-Alive: timeout=2")
    ResponseEntity<String> checkUrl(URI address) throws UnknownHostException;
}
