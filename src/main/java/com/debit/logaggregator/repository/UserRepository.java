package com.debit.logaggregator.repository;

import com.debit.logaggregator.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Bogdan Lesin
 */
@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findUserByUsername(String username);
}
