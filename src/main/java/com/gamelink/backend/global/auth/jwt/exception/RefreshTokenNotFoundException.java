package com.gamelink.backend.global.auth.jwt.exception;

import com.gamelink.backend.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class RefreshTokenNotFoundException extends LocalizedMessageException {
    public RefreshTokenNotFoundException() {
        super(HttpStatus.NOT_FOUND, "notfound.refresh-token");
    }
}
