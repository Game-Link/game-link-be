package com.gamelink.backend.infra.s3.exception;

import com.gamelink.backend.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class InvalidFileContentTypeException extends LocalizedMessageException {
     public InvalidFileContentTypeException(Throwable e) {
         super(e, HttpStatus.BAD_REQUEST, "invalid.file-content-type");
     }
}
