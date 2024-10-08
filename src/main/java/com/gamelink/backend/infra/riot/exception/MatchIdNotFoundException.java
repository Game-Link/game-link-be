package com.gamelink.backend.infra.riot.exception;

import com.gamelink.backend.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class MatchIdNotFoundException extends LocalizedMessageException {

    public MatchIdNotFoundException() {
        super(HttpStatus.NOT_FOUND, "notfound.match-id");
    }
}
