package com.debit.logaggregator.service;

import com.debit.logaggregator.dto.UserUrlDTO;
import com.debit.logaggregator.entity.UserUrl;
import com.debit.logaggregator.service.defaultInterface.CrudServiceWithAuth;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserUrlService extends CrudServiceWithAuth<UserUrlDTO, UUID, UUID> {
    public List<UserUrlDTO> getAll(UUID userId);
}
