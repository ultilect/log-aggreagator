package com.debit.logaggregator.service.defaultInterface;

import java.util.Optional;

public interface CrudServiceWithAuth<T,V,E> {
    public void saveEntity(T entity, E userId);

    public Optional<T> getEntity(V id, E userId);

    public Optional<T> updateEntity(V id, T newEntity, E userId);

    public void deleteEntity(V id, E userId);
}