package com.debit.logaggregator.dto;

import com.debit.logaggregator.entity.User;
import com.debit.logaggregator.entity.UserUrl;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Формат пользователя, возвращаемый клиенту.
 * @author Bogdan Lesin
 * @param id id пользователя
 * @param username логин пользователя
 * @param email почта пользователя
 * @param phone номер телефона пользователя(необязательно)
 */
public record UserDTO(
        @JsonProperty("id") UUID id,
        @JsonProperty("username") String username,
        @JsonProperty("email") String email,
        @JsonProperty("phone") String phone,
        @JsonProperty("url") @JsonIgnoreProperties("user") List<UserUrl> urls
        ) {
        public UserDTO() {
                this(UUID.randomUUID(), "test", "test@email.com", "88889988", new ArrayList<>());
        }
        public UserDTO(final User user) {
                this(user.getId(), user.getUsername(), user.getEmail(), user.getPhone(), user.getUserUrls());
        }
}
