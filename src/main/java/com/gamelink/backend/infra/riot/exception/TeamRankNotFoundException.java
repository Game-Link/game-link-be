package com.gamelink.backend.infra.riot.exception;

import com.gamelink.backend.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class TeamRankNotFoundException extends LocalizedMessageException {
    public TeamRankNotFoundException() {
        super(HttpStatus.NOT_FOUND, "notfound.team-rank");
    }
}
