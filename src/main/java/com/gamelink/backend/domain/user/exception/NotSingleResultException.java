package com.gamelink.backend.domain.user.exception;

import com.gamelink.backend.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class NotSingleResultException extends LocalizedMessageException {

    public NotSingleResultException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "db.error");
    }
}
