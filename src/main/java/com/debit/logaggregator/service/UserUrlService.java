package com.debit.logaggregator.service;

import com.debit.logaggregator.dto.UserUrlDTO;
import com.debit.logaggregator.service.defaultinterface.CrudServiceWithAuth;

import java.util.List;
import java.util.UUID;
/**
 * Service interface for interacting with user endpoints for logs.
 */
public interface UserUrlService extends CrudServiceWithAuth<UserUrlDTO, UUID, UUID> {
    List<UserUrlDTO> getAll(UUID userId);
}
