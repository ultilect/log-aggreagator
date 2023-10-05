package com.debit.logaggregator.service.impl;

import com.debit.logaggregator.client.UserUrlClient;
import com.debit.logaggregator.dto.UserUrlDTO;
import com.debit.logaggregator.entity.User;
import com.debit.logaggregator.entity.UserUrl;
import com.debit.logaggregator.repository.UserRepository;
import com.debit.logaggregator.repository.UserUrlRepository;
import com.debit.logaggregator.service.UserUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Bogdan Lesin
 */
@Service
@SuppressWarnings({"ClassDataAbstractionCoupling", "PMD.PreserveStackTrace"})
public class UserUrlServiceImpl implements UserUrlService {
    private static final String BAD_URL_RESPONSE = "Bad url";
    private final UserUrlRepository userUrlRepository;
    private final UserRepository userRepository;

    private final UserUrlClient userUrlClient;

    @Autowired
    public UserUrlServiceImpl(final UserUrlRepository userUrlRepository,
                              final UserRepository userRepository,
                              final UserUrlClient userUrlClient) {
        this.userUrlRepository = userUrlRepository;
        this.userRepository = userRepository;
        this.userUrlClient = userUrlClient;
    }

    @Override
    public Optional<UserUrlDTO> saveEntity(final UserUrlDTO entity, final UUID userId)
            throws NoSuchElementException, URISyntaxException, UnknownHostException {
        //TODO: Exception handling(unique fields)
        final boolean checkUri = checkURIAndResponse(entity.url());
        if (!checkUri) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, BAD_URL_RESPONSE);
        }
        final UserUrl newUserUrl = new UserUrl();
        final User user = this.userRepository.findById(userId).orElseThrow();
        newUserUrl.updateWithoutId(entity);
        newUserUrl.setUser(user);
        newUserUrl.setCreatedAt(new Date());
        final UserUrl userUrl = this.userUrlRepository.save(newUserUrl);
        return Optional.of(new UserUrlDTO(userUrl));
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
            try {
                final boolean checkUrl = checkURIAndResponse(newEntity.url());
                if (!checkUrl) {
                    throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, BAD_URL_RESPONSE);
                }
            } catch (Exception e) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, BAD_URL_RESPONSE);
            }
            userUrl.updateWithoutId(newEntity);
            return new UserUrlDTO(this.userUrlRepository.save(userUrl));
        });
    }

    @Override
    public void deleteEntity(final UUID id, final UUID userId) {
        this.userUrlRepository.findByIdAndUserId(id, userId).ifPresent(this.userUrlRepository::delete);
    }

    private boolean checkURIAndResponse(final String uri) throws URISyntaxException, UnknownHostException {
        final URI userUri = new URI(uri);
        final ResponseEntity<?> uriResponse = this.userUrlClient.checkUrl(userUri);
        return uriResponse.getStatusCode() == HttpStatus.OK;
    }
}
