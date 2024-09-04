package com.gamelink.backend.global.error.exception;

import org.springframework.http.HttpStatus;

public class ErrorLogNotFoundException extends LocalizedMessageException{

    public ErrorLogNotFoundException() {
        super(HttpStatus.NOT_FOUND, "notfound.error-log");
    }
}
