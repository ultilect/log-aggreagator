package com.debit.logaggregator.repository;

import com.debit.logaggregator.entity.UserUrl;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Bogdan Lesin
 */
@Repository
public interface UserUrlRepository extends CrudRepository<UserUrl, UUID> {
}
