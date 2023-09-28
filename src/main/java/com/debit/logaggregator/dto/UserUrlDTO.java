package com.debit.logaggregator.dto;

import com.debit.logaggregator.entity.UserUrl;
import jakarta.annotation.Nullable;

import java.util.Date;
import java.util.UUID;

/**
 * @param id - unique id
 * @param url - url for getting logs
 * @param comment - some description of endpoint
 * @param nextRequestTime - date for next request sending
 * @param periodInMinutes - interval between sending request
 */
//TODO: validation
public record UserUrlDTO(
        @Nullable
        UUID id,
        String url,
        String comment,
        Date nextRequestTime,
        Integer periodInMinutes
) {
        public UserUrlDTO(final UserUrl userUrl) {
                this(userUrl.getId(),
                        userUrl.getUrl(),
                        userUrl.getComment(),
                        userUrl.getNextRequestTime(),
                        userUrl.getPeriodInMinutes());
        }
}
