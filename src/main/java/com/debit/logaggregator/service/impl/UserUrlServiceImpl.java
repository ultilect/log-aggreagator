package com.debit.logaggregator.service.impl;

import com.debit.logaggregator.client.UserUrlClient;
import com.debit.logaggregator.dto.UserUrlDTO;
import com.debit.logaggregator.entity.User;
import com.debit.logaggregator.entity.UserUrl;
import com.debit.logaggregator.repository.UserRepository;
import com.debit.logaggregator.repository.UserUrlRepository;
import com.debit.logaggregator.service.UserUrlService;
import feign.Feign;
import feign.Target;
import feign.codec.Decoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClientProperties;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Bogdan Lesin
 */
@Service
@Import(FeignClientProperties.FeignClientConfiguration.class)
@SuppressWarnings("ClassDataAbstractionCoupling")
public class UserUrlServiceImpl implements UserUrlService {
    private final UserUrlRepository userUrlRepository;
    private final UserRepository userRepository;

    private final UserUrlClient userUrlClient;

    @Autowired
    public UserUrlServiceImpl(final UserUrlRepository userUrlRepository,
                              final UserRepository userRepository) {
        this.userUrlRepository = userUrlRepository;
        this.userRepository = userRepository;
        this.userUrlClient = Feign.builder()
                .encoder(new SpringFormEncoder())
                .decoder(new ResponseEntityDecoder(new Decoder.Default()))
                .target(Target.EmptyTarget.create(UserUrlClient.class));
    }

    @Override
    public void saveEntity(final UserUrlDTO entity, final UUID userId)
            throws NoSuchElementException, URISyntaxException {
        //TODO: Exception handling(unique fields)
        final URI userUri = new URI(entity.url());
        final ResponseEntity<?> uriResponce = this.userUrlClient.checkUrl(userUri);
        if (uriResponce.getStatusCode() != HttpStatus.OK) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }
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
