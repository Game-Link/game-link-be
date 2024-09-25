package com.gamelink.backend.infra.s3.exception;

import com.gamelink.backend.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class InvalidAccessObjectStorageException extends LocalizedMessageException {
    public InvalidAccessObjectStorageException(Throwable e) {
        super(e, HttpStatus.BAD_REQUEST, "invalid.access-s3");
    }
}
