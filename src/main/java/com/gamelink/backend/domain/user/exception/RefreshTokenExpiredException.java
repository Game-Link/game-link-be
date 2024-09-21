package com.gamelink.backend.domain.user.exception;

import com.gamelink.backend.global.error.CustomHttpStatus;
import com.gamelink.backend.global.error.exception.LocalizedMessageException;

public class RefreshTokenExpiredException extends LocalizedMessageException {
    public RefreshTokenExpiredException() {
        super(CustomHttpStatus.REFRESH_TOKEN_EXPIRED, "expired.refresh-token");
    }
}
