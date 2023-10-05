package com.debit.logaggregator.config;

import com.debit.logaggregator.client.UserUrlClient;
import feign.Contract;
import feign.Feign;
import feign.Target;
import feign.codec.Decoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@EnableFeignClients(basePackages = "com.debit.logaggregator")
@Configuration
@Import(FeignClientProperties.FeignClientConfiguration.class)
@SuppressWarnings("MissingJavadocType")
public class FeignConfig {

    @Bean
    public Contract useFeignAnnotations() {
        return new Contract.Default();
    }

    @Bean
    @Primary
    public UserUrlClient addUserUrlClient() {
        return Feign.builder()
                .encoder(new SpringFormEncoder())
                .decoder(new ResponseEntityDecoder(new Decoder.Default()))
                .target(Target.EmptyTarget.create(UserUrlClient.class));
    }
}
