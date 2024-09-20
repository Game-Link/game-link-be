package com.gamelink.backend.infra.riot.exception;

import com.gamelink.backend.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class RiotUserNotFoundException extends LocalizedMessageException {
    public RiotUserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "notfound.riotuser");
    }
}
