package com.gamelink.backend.infra.riot.exception;

import com.gamelink.backend.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class CannotCrawlingException extends LocalizedMessageException {
    public CannotCrawlingException() {
        super(HttpStatus.FORBIDDEN, "cannot.crawling");
    }
}
