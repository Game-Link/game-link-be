package com.gamelink.backend.global.error.exception;

import org.springframework.http.HttpStatus;

public class NotGrantedException extends LocalizedMessageException {
    public NotGrantedException() {
        super(HttpStatus.FORBIDDEN, "required.granted");
    }

    public NotGrantedException(HttpStatus status, String messageId, Object... arguments) {
        super(status, messageId, arguments);
    }

    public NotGrantedException(Throwable t) {
        super(t, HttpStatus.FORBIDDEN, "required.granted");
    }
}
