package com.debit.logaggregator.service.defaultInterface;

import java.util.Optional;

public interface CrudService<T,V> {
    public void saveEntity(T entity);

    public Optional<T> getEntity(V id);

    public Optional<T> updateEntity(V id, T newEntity);

    public void deleteEntity(V id);
}
