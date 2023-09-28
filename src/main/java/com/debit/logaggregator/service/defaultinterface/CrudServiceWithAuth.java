package com.debit.logaggregator.service.defaultinterface;

import java.util.Optional;

/**
 * Interface for entities connected to authorized user.
 * @param <T> - Entity class
 * @param <V> - Entity id type
 * @param <E> - Authorized user id
 */
public interface CrudServiceWithAuth<T, V, E> {
    void saveEntity(T entity, E userId);

    Optional<T> getEntity(V id, E userId);

    Optional<T> updateEntity(V id, T newEntity, E userId);

    void deleteEntity(V id, E userId);
}
