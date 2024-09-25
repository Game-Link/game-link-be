package com.gamelink.backend.infra.s3.exception;

import com.gamelink.backend.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.InvalidMediaTypeException;

public class InvalidImageContentTypeException extends LocalizedMessageException {
    public InvalidImageContentTypeException(Throwable e) {
        super(e, HttpStatus.BAD_REQUEST, "invalid.image-content-type");
    }
}
