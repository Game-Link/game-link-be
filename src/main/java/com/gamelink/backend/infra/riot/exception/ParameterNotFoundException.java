package com.gamelink.backend.infra.riot.exception;

import com.gamelink.backend.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class ParameterNotFoundException extends LocalizedMessageException {
    public ParameterNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
