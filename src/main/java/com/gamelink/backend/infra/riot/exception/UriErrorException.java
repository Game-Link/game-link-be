package com.gamelink.backend.infra.riot.exception;

import com.gamelink.backend.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class UriErrorException extends LocalizedMessageException {
    public UriErrorException() {
        super(HttpStatus.BAD_REQUEST, "invalid.uri");
    }
}
