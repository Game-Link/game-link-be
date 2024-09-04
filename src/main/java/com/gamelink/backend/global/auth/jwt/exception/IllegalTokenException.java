package com.gamelink.backend.global.auth.jwt.exception;

import io.jsonwebtoken.JwtException;

public class IllegalTokenException extends JwtException {
    public IllegalTokenException(String message) {
        super(message);
    }
}
