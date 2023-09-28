package com.debit.logaggregator.service.impl;

import com.debit.logaggregator.dto.UserUrlDTO;
import com.debit.logaggregator.entity.User;
import com.debit.logaggregator.entity.UserUrl;
import com.debit.logaggregator.repository.UserRepository;
import com.debit.logaggregator.repository.UserUrlRepository;
import com.debit.logaggregator.service.UserUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Bogdan Lesin
 */
@Service
public class UserUrlServiceImpl implements UserUrlService {
    private final UserUrlRepository userUrlRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserUrlServiceImpl(final UserUrlRepository userUrlRepository,
                              final UserRepository userRepository) {
        this.userUrlRepository = userUrlRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveEntity(final UserUrlDTO entity, final UUID userId) throws NoSuchElementException {
        //TODO: test of url; Exception handling(unique fields)
        final UserUrl newUserUrl = new UserUrl();
        final User user = this.userRepository.findById(userId).orElseThrow();
        newUserUrl.updateWithoutId(entity);
        newUserUrl.setUser(user);
        newUserUrl.setCreatedAt(new Date());
        this.userUrlRepository.save(newUserUrl);
    }

    @Override
    public Optional<UserUrlDTO> getEntity(final UUID id, final UUID userId) {
        return this.userUrlRepository.findByIdAndUserId(id, userId).map(UserUrlDTO::new);
    }

    @Override
    public List<UserUrlDTO> getAll(final UUID userId) {
        return this.userUrlRepository
                .findAllByUserId(userId)
                .stream().map(UserUrlDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserUrlDTO> updateEntity(final UUID id, final UserUrlDTO newEntity, final UUID userId) {
        return this.userUrlRepository.findByIdAndUserId(id, userId).map((userUrl) -> {
            userUrl.updateWithoutId(newEntity);
            return new UserUrlDTO(this.userUrlRepository.save(userUrl));
        });
    }

    @Override
    public void deleteEntity(final UUID id, final UUID userId) {
        this.userUrlRepository.findByIdAndUserId(id, userId).ifPresent(this.userUrlRepository::delete);
    }
}
