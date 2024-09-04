package com.gamelink.backend.global.auth.jwt.exception;

import com.gamelink.backend.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class JwtTokenNotFoundException extends LocalizedMessageException {

    public JwtTokenNotFoundException() {
        super(HttpStatus.NOT_FOUND, "notfound.jwt-token");
    }
}
