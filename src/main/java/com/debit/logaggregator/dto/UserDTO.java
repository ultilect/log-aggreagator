package com.debit.logaggregator.dto;

import com.debit.logaggregator.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;
/**
 * Формат пользователя, возвращаемый клиенту.
 * @author Bogdan Lesin
 * @param id id пользователя
 * @param email почта пользователя
 * @param phone номер телефона пользователя(необязательно)
 */
public record UserDTO(
        @JsonProperty("id") UUID id,
        @JsonProperty("username") String username,
        @JsonProperty("email") String email,
        @JsonProperty("phone") String phone
        ) {
        public UserDTO() {
                this(UUID.randomUUID(), "test","test@email.com", "88889988");
        }
        public UserDTO(final User user) {
                this(user.getId(), user.getUsername(), user.getEmail(), user.getPhone());
        }
}
