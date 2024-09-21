package com.gamelink.backend.infra.riot.exception;

import com.gamelink.backend.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class SoloRankNotFoundException extends LocalizedMessageException {
    public SoloRankNotFoundException() {
        super(HttpStatus.NOT_FOUND, "notfound.solo-rank");
    }
}
