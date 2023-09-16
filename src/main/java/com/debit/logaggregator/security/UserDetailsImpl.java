package com.debit.logaggregator.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.debit.logaggregator.entity.User;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Bogdan Lesin
 */
public class UserDetailsImpl implements UserDetails {
    private final String username;
    private final String password;

    private final UUID id;
    UserDetailsImpl(final String username, final String password, final UUID id) {
        this.username = username;
        this.password = password;
        this.id = id;
    }

    public static UserDetailsImpl build(final User user) {
        return new UserDetailsImpl(user.getUsername(), user.getPassword(), user.getId()) {
        };
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new LinkedList<>();
    }

    public UUID getId() {
        return this.id;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final UserDetailsImpl that = (UserDetailsImpl) o;
        return Objects.equals(username, that.username) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }

    @Override
    public String toString() {
        return "UserDetailsImpl{"
                + "username='" + username + '\''
                + ", password='" + password + '\''
                + '}';
    }
}
