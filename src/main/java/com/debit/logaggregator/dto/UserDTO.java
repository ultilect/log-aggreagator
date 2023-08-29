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
        @JsonProperty("email") String email,
        @JsonProperty("phone") String phone
        ) {
        public UserDTO() {
                this(UUID.randomUUID(), "test@email.com", "88889988");
        }
        public UserDTO(final User user) {
                this(user.getId(), user.getEmail(), user.getPhone());
        }
}
