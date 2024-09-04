package com.gamelink.backend.global.error;

import com.gamelink.backend.global.error.exception.LocalizedMessageException;
import lombok.Getter;
import org.springframework.context.MessageSource;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Getter
public class ErrorResponse {
    private final String timestamp;
    private final String trackingId;
    private final int statusCode;
    private final String status;
    private final String code;
    private final Object message;

    public ErrorResponse(MessageSource messageSource, Locale locale, LocalizedMessageException e) {
        this.timestamp = LocalDateTime.now().toString();
        this.trackingId = UUID.randomUUID().toString();
        this.statusCode = e.getStatusCode();
        this.status = e.getStatus();
        this.code = e.getCode();
        this.message = e.getMessages(messageSource, locale).get(0);
    }
}
