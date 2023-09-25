package com.debit.logaggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@SuppressWarnings({"PMD.UseUtilityClass", "HideUtilityClassConstructor", "MissingJavadocType"})
public class LogAggregatorApplication {

    public static void main(final String[] args) {
        SpringApplication.run(LogAggregatorApplication.class, args);
    }

}
