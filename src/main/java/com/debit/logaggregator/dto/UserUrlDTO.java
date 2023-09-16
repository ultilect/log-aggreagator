package com.debit.logaggregator.dto;

import java.util.Date;
import java.util.UUID;

public record UserUrlDTO(
        UUID id,
        String url,
        String comment,
        Date createdAt,
        Date nextRequestTime,
        Integer periodInMinutes
) {
}
