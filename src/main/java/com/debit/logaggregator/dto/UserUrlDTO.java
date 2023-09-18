package com.debit.logaggregator.dto;

import com.debit.logaggregator.entity.UserUrl;
import jakarta.annotation.Nullable;

import java.util.Date;
import java.util.UUID;

//TODO: validation
public record UserUrlDTO(
        @Nullable
        UUID id,
        String url,
        String comment,
        Date nextRequestTime,
        Integer periodInMinutes
) {
        public UserUrlDTO(UserUrl userUrl) {
                this(userUrl.getId(), userUrl.getUrl(), userUrl.getComment(), userUrl.getNextRequestTime(), userUrl.getPeriodInMinutes());
        }
}
