package com.deneb.logaggregator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public record UserDTO(
        @JsonProperty("id") UUID id,
        @JsonProperty("email") String email,
        @JsonProperty("phone") String phone
        ) {
        public UserDTO() {
                this(UUID.randomUUID(),"test@email.com","88889988");
        }
}
