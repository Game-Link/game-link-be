package com.gamelink.backend.infra.riot.exception;

import com.gamelink.backend.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class RiotUserAlreadyExistException extends LocalizedMessageException {
    public RiotUserAlreadyExistException() {
        super(HttpStatus.FORBIDDEN, "already.riot-user");
    }
}
