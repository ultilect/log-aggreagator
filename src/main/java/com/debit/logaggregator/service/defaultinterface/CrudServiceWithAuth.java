package com.debit.logaggregator.service.defaultinterface;

import java.util.Optional;

/**
 * Interface for entities connected to authorized user.
 * @param <T> - Entity class
 * @param <V> - Entity id type
 * @param <E> - Authorized user id
 */
public interface CrudServiceWithAuth<T, V, E> {
    Optional<T> saveEntity(T entity, E userId) throws Exception;

    Optional<T> getEntity(V id, E userId);

    Optional<T> updateEntity(V id, T newEntity, E userId) throws Exception;

    void deleteEntity(V id, E userId);
}
