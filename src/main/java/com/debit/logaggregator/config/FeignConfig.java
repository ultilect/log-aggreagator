package com.debit.logaggregator.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackages = "com.debit.logaggregator")
@Configuration
@SuppressWarnings("MissingJavadocType")
public class FeignConfig {
}
