package com.gamelink.backend.infra.riot.exception;

import com.gamelink.backend.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class RiotUserNotMatchException extends LocalizedMessageException {
    public RiotUserNotMatchException() {
        super(HttpStatus.FORBIDDEN, "notmatch.riotuser");
    }
}
