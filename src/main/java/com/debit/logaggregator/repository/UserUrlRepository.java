package com.debit.logaggregator.repository;

import com.debit.logaggregator.entity.UserUrl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Bogdan Lesin
 */
@Repository
public interface UserUrlRepository extends CrudRepository<UserUrl, UUID> {
    Optional<UserUrl> findByIdAndUserId(UUID id, UUID userId);
    List<UserUrl> findAllByUserId(UUID userId);
}
