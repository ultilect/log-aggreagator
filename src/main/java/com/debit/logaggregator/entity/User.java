package com.debit.logaggregator.entity;

import com.debit.logaggregator.util.global.SuppressFBWarnings;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Представление пользователя в БД.
 */
@SuppressFBWarnings("DLS_DEAD_LOCAL_STORE")
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = 50, unique = true, nullable = false)
    private String username;
    @Column(length = 50, unique = true, nullable = false)
    private String email;

    @Column(length = 30, unique = true)
    private String phone;

    @Column(nullable = false)
    private String password;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = UserUrl.class)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("user")
    private List<UserUrl> userUrls;
    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(final String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public List<UserUrl> getUserUrls() {
        return userUrls;
    }

    public void setUserUrls(final List<UserUrl> userUrls) {
        this.userUrls = userUrls;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final User user = (User) o;
        return Objects.equals(id, user.id)
                && Objects.equals(username, user.username)
                && Objects.equals(email, user.email)
                && Objects.equals(phone, user.phone)
                && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, phone, password);
    }

    @Override
    public String toString() {
        return "User{"
                + "id=" + id
                + ", username='" + username + '\''
                + ", email='" + email + '\''
                + ", phone='" + phone + '\''
                + ", password='" + password + '\''
                + '}';
    }
}
