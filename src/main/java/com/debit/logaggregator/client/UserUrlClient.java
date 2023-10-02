package com.debit.logaggregator.client;

import feign.Headers;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;

import java.net.URI;

/**
 * Client is used for requesting user endpoints.
 */
@FeignClient("userUrl")
public interface UserUrlClient {

    @RequestLine("HEAD")
    @Headers("Keep-Alive: timeout=2")
    ResponseEntity<String> checkUrl(URI address);
}
