package com.debit.logaggregator.service.defaultinterface;

import java.util.Optional;

/**
 * Simple CRUD interface for service.
 * @param <T> - Entity class
 * @param <V> - Entity id type
 */
public interface CrudService<T, V> {
    void saveEntity(T entity);

    Optional<T> getEntity(V id);

    Optional<T> updateEntity(V id, T newEntity);

    void deleteEntity(V id);
}
