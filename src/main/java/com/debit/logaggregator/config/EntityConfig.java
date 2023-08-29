package com.debit.logaggregator.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EntityConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
