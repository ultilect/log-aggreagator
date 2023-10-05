package com.debit.logaggregator.service;

import com.debit.logaggregator.dto.UserUrlDTO;
import com.debit.logaggregator.service.defaultinterface.CrudServiceWithAuth;

import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
/**
 * Service interface for interacting with user endpoints for logs.
 */
public interface UserUrlService extends CrudServiceWithAuth<UserUrlDTO, UUID, UUID> {
    @Override
    Optional<UserUrlDTO> saveEntity(UserUrlDTO entity, UUID userId)
            throws URISyntaxException, UnknownHostException;

    @Override
    Optional<UserUrlDTO> updateEntity(UUID id, UserUrlDTO newEntity, UUID userId)
            throws URISyntaxException, UnknownHostException;
    List<UserUrlDTO> getAll(UUID userId);
}
